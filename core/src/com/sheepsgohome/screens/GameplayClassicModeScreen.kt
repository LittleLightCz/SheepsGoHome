package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.*
import com.sheepsgohome.GameData.*
import com.sheepsgohome.GameMusic.ambient
import com.sheepsgohome.GameObjectType.*
import com.sheepsgohome.GameTools.calculateAngle
import com.sheepsgohome.GameTools.setRandomMovement
import java.util.*

class GameplayClassicModeScreen : Screen, ContactListener {

    internal enum class State {
        eRunning, eGameOver_Wild, eGameOver_Hungry, eGameOver_Alpha, eNextLevel
    }

    private var fpsLogger: FPSLogger? = null

    private lateinit var touchpad: Touchpad
    private var touchpadEnabled: Boolean = false

    private var wolves_count: Int = 0

    private lateinit var stage: Stage
    private lateinit var levelLabel: Label

    private var gameState: State? = null

    private lateinit var batch: SpriteBatch

    private lateinit var sheep_texture: Texture
    private lateinit var wolf_texture: Texture
    private lateinit var hungry_wolf_texture: Texture
    private lateinit var alpha_wolf_texture: Texture
    private lateinit var home_texture: Texture
    private lateinit var background_texture: Texture

    private lateinit var wolves: List<Sprite>
    private lateinit var sheep: Sprite
    private lateinit var home: Sprite
    private lateinit var background: Sprite

    override fun show() {
        //catch menu and back buttons
        Gdx.input.isCatchBackKey = true
        Gdx.input.setCatchMenuKey(true)

        //FPS debug
        fpsLogger = FPSLogger()

        //Ambient
        if (ambient != null && SOUND_ENABLED && !ambient.isPlaying) {
            ambient.play()
        }

        //Stage
        val multiplier = 1f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        //Touchpad
        touchpadEnabled = VIRTUAL_JOYSTICK != VIRTUAL_JOYSTICK_NONE

        touchpad = Touchpad(0f, GameSkins.skin)
        val touchPadSize = 30
        if (VIRTUAL_JOYSTICK == VIRTUAL_JOYSTICK_RIGHT) {
            touchpad.setBounds(CAMERA_WIDTH - touchPadSize, 0f, touchPadSize.toFloat(), touchPadSize.toFloat())
        } else if (VIRTUAL_JOYSTICK == VIRTUAL_JOYSTICK_LEFT) {
            touchpad.setBounds(0f, 0f, touchPadSize.toFloat(), touchPadSize.toFloat())
        }

        touchpad.addAction(Actions.alpha(0.5f))

        levelLabel = Label(Loc.format("level", LEVEL), GameSkins.skin, "levelTitle")
        val fontScale = (CAMERA_WIDTH * multiplier - 40) / levelLabel.prefWidth
        levelLabel.setFontScale(fontScale)

        val table = Table()
        table.add<Label>(levelLabel)
        table.setFillParent(true)

        levelLabel.addAction(Actions.sequence(
                Actions.alpha(1f),
                Actions.fadeOut(3f)
        ))

        stage.addActor(table)
        if (touchpadEnabled) {
            stage.addActor(touchpad)
        }
        Gdx.input.inputProcessor = stage
        //-------------

        gameState = State.eRunning

        camera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
        batch = SpriteBatch()

        SetUpLevel()

        StartWolves()
    }

    private fun SetUpLevel() {
        sheep_texture = Texture("sheep.png")
        home_texture = Texture("home.png")
        wolf_texture = Texture("wolf.png")
        hungry_wolf_texture = Texture("wolf-hungry.png")
        alpha_wolf_texture = Texture("wolf-alpha.png")

        background_texture = Texture("grass-background.jpg")
        background_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        background = Sprite(background_texture)
        background.setPosition(-CAMERA_WIDTH, -CAMERA_HEIGHT)
        background.setSize(CAMERA_WIDTH * 2, CAMERA_HEIGHT * 2)

        sheep = Sprite(sheep_texture)
        sheep.setSize(SHEEP_SIZE, SHEEP_SIZE)
        sheep.setOriginCenter()

        home = Sprite(home_texture)
        home.setSize(HOME_SIZE, HOME_SIZE)
        home.setPosition(-home.width / 2, CAMERA_HEIGHT / 2 - home.height)

        InitializePhysics()

        CreateSheepBody()
        CreateHomeBody()
        CreateWallBoundaries()

        wolves_count = CreateWolfBodies()

        wolves = wolf_bodies.map { it.userData as GameObjectType }
                .map { wolfType ->
                    when (wolfType) {
                        eHungryWolf -> Sprite(hungry_wolf_texture).apply {
                            setSize(HUNGRY_WOLF_SIZE, HUNGRY_WOLF_SIZE)
                        }
                        eAlphaWolf -> Sprite(alpha_wolf_texture).apply {
                            setSize(ALPHA_WOLF_SIZE, ALPHA_WOLF_SIZE)
                        }
                        else -> Sprite(wolf_texture).apply {
                            setSize(WILD_WOLF_SIZE, WILD_WOLF_SIZE)
                        }
                    }.apply { setOriginCenter() }
                }
    }

    fun InitializePhysics() {
        Box2D.init()

        world = World(Vector2(0f, 0f), true)
        world.setContactListener(this)

        debugRenderer = Box2DDebugRenderer()
    }

    override fun render(delta: Float) {
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        when (gameState) {
            GameplayClassicModeScreen.State.eRunning -> {
                //positioning
                sheep.setPosition(
                        sheep_body.position.x - SHEEP_SIZE / 2,
                        sheep_body.position.y - SHEEP_SIZE / 2
                )

                sheep.rotation = Math.toDegrees(sheep_body.body.angle.toDouble()).toFloat()

                //input
                if (touchpadEnabled) {
                    if (touchpad.isTouched) {
                        handleTouch()
                    } else {
                        sheep_body.body.setLinearVelocity(0f, 0f)
                        sheep_body.body.angularVelocity = 0f
                    }

                } else {
                    if (Gdx.input.isTouched) {
                        handleTouch(Gdx.input.x, Gdx.input.y)
                    } else {
                        sheep_body.body.setLinearVelocity(0f, 0f)
                        sheep_body.body.angularVelocity = 0f
                    }
                }

                //drawing
                batch.projectionMatrix = camera.combined
                batch.begin()

                background.draw(batch)

                sheep.draw(batch)

                //draw home - top center
                home.draw(batch)

                //draw wolves
                var wolfType: GameObjectType
                for (i in 0..wolves_count - 1) {
                    wolfType = wolf_bodies[i].userData as GameObjectType

                    when (wolfType) {
                        eAlphaWolf -> {
                            wolves[i].setPosition(
                                    wolf_bodies[i].position.x - ALPHA_WOLF_SIZE / 2,
                                    wolf_bodies[i].position.y - ALPHA_WOLF_SIZE / 2
                            )

                            setAlphaWolfVelocity(wolf_bodies[i].body)
                        }
                        eHungryWolf -> {
                            wolf_bodies[i].calculateSteeringBehaviour()
                            wolves[i].setPosition(
                                    wolf_bodies[i].position.x - HUNGRY_WOLF_SIZE / 2,
                                    wolf_bodies[i].position.y - HUNGRY_WOLF_SIZE / 2
                            )
                        }
                        else -> wolves[i].setPosition(
                                wolf_bodies[i].position.x - WILD_WOLF_SIZE / 2,
                                wolf_bodies[i].position.y - WILD_WOLF_SIZE / 2
                        )
                    }

                    val targetVelocity = wolf_bodies[i].linearVelocity
                    wolves[i].rotation = calculateAngle(targetVelocity)

                    wolves[i].draw(batch)
                }

                batch.end()

                //                debugRenderer.render(world, camera.combined);
                world.step(graphics.deltaTime, 6, 2)

                stage.act()
                stage.draw()
            }

            GameplayClassicModeScreen.State.eGameOver_Wild -> gameOver(GameDialogType.typeSheepFailed_Wild)
            GameplayClassicModeScreen.State.eGameOver_Hungry -> gameOver(GameDialogType.typeSheepFailed_Hungry)
            GameplayClassicModeScreen.State.eGameOver_Alpha -> gameOver(GameDialogType.typeSheepFailed_Alpha)
            GameplayClassicModeScreen.State.eNextLevel -> nextLevel()
        }

        //fps debug
        //        fpsLogger.log();

    }

    private fun setAlphaWolfVelocity(wolf: Body) {
        val vec = Vector2(sheep_body.position).sub(wolf.position).nor()
        val alphaSpeed = ALPHA_WOLF_SPEED / Math.abs(wolf.position.dst(sheep_body.position) / CAMERA_HEIGHT)
        wolf.setLinearVelocity(vec.x * alphaSpeed, vec.y * alphaSpeed)
    }

    override fun resize(width: Int, height: Int) {

        stage.viewport.update(width, height, true)

        //world boundaries
        walls_bodies[0].setTransform(-CAMERA_WIDTH / 2, 0f, 0f)
        walls_bodies[1].setTransform(CAMERA_WIDTH / 2, 0f, 0f)
        walls_bodies[2].setTransform(0f, CAMERA_HEIGHT / 2, (Math.PI / 2f).toFloat())
        walls_bodies[3].setTransform(0f, -CAMERA_HEIGHT / 2, (Math.PI / 2f).toFloat())

        //sheep
        sheep_body.body.setTransform(0f, -CAMERA_HEIGHT / 2f + 2, (Math.PI / 2f).toFloat())

        //home
        home_body.body.setTransform(0f, CAMERA_HEIGHT / 2f - HOME_SIZE / 2, 0f)

        //wolves
        val gap = 0.5f
        val start_offset = 6f

        var x = start_offset + -CAMERA_WIDTH / 2
        var y = CAMERA_HEIGHT / 2 - HOME_SIZE - start_offset

        val max_wolves_in_a_row = 13

        val angle = (Math.PI * 3f / 2f).toFloat()

        for (i in wolf_bodies.indices) {
            wolf_bodies[i].body.setTransform(x + i % max_wolves_in_a_row * (gap + WILD_WOLF_SIZE), y, angle)

            if (i != 0 && i % max_wolves_in_a_row == max_wolves_in_a_row - 1) {
                y -= WILD_WOLF_SIZE + gap
            }
        }

        //center last row
        var i = wolves_count - wolves_count % max_wolves_in_a_row
        val w_width = wolves_count % max_wolves_in_a_row * (gap + WILD_WOLF_SIZE)

        x += (CAMERA_WIDTH - w_width) / 2
        x -= start_offset / 2

        while (i < wolf_bodies.size) {
            wolf_bodies[i].body.setTransform(x + i % max_wolves_in_a_row * (gap + WILD_WOLF_SIZE), y, angle)
            i++
        }
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        //stop ambient
        if (ambient != null && ambient.isPlaying) {
            ambient.pause()
        }

        //release menu and back buttons
        Gdx.input.isCatchBackKey = false
        Gdx.input.setCatchMenuKey(false)

        dispose()
    }

    override fun dispose() {
        sheep_texture.dispose()
        wolf_texture.dispose()
        hungry_wolf_texture.dispose()
        alpha_wolf_texture.dispose()
        home_texture.dispose()
        background_texture.dispose()

        batch.dispose()
        world.dispose()
        stage.dispose()
    }

    private fun CreateWildWolfBody(): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(0f, 0f)

        val circleShape = CircleShape()
        circleShape.radius = WILD_WOLF_SIZE / 2 * 0.85f

        val fixtureDef = FixtureDef()
        fixtureDef.shape = circleShape
        fixtureDef.density = 0.1f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f

        return CreateWolfBody(eWildWolf, bodyDef, fixtureDef)
    }

    private fun CreateHungryWolfBody(): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(0f, 0f)

        val circleShape = CircleShape()
        circleShape.radius = HUNGRY_WOLF_SIZE / 2 * 0.85f

        val fixtureDef = FixtureDef()
        fixtureDef.shape = circleShape
        fixtureDef.density = 0.2f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f

        return CreateWolfBody(eHungryWolf, bodyDef, fixtureDef)
    }

    private fun CreateAlphaWolfBody(): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(0f, 0f)

        val circleShape = CircleShape()
        circleShape.radius = ALPHA_WOLF_SIZE / 2 * 0.85f

        val fixtureDef = FixtureDef()
        fixtureDef.shape = circleShape
        fixtureDef.density = 0.3f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f

        return CreateWolfBody(eAlphaWolf, bodyDef, fixtureDef)
    }

    private fun CreateWolfBody(wolfType: GameObjectType, bodyDef: BodyDef, fixtureDef: FixtureDef): Body {

        val body = world.createBody(bodyDef)
        body.userData = wolfType
        body.createFixture(fixtureDef)

        fixtureDef.shape.dispose()

        return body
    }


    private fun CreateWolfBodies(): Int {

        val data = getWolvesData(LEVEL)

        val count = data.wolvesCount

        val wolf_bodies_list = ArrayList<SteerableBody>()

        for (i in 0..data.WildWolves - 1) {
            wolf_bodies_list.add(SteerableBody(CreateWildWolfBody()))
        }

        for (i in 0..data.HungryWolves - 1) {
            wolf_bodies_list.add(SteerableHungryWolfBody(CreateHungryWolfBody(), sheep_body, home_body))
        }

        for (i in 0..data.AlphaWolves - 1) {
            wolf_bodies_list.add(SteerableBody(CreateAlphaWolfBody()))
        }

        Collections.shuffle(wolf_bodies_list)
        wolf_bodies = wolf_bodies_list.toTypedArray()

        return count
    }

    private fun getWolvesData(level: Int): WolvesData {
        val data = WolvesData()

        data.WildWolves = ((level - 1) % 5 + 1) * 5

        if (level < 6) {
            data.HungryWolves = 0
        } else {
            data.HungryWolves = (level - 1 - 5) / 5 % 4 + 1
        }

        if (level < 26) {
            data.AlphaWolves = 0
        } else {
            data.AlphaWolves = (level - 1 - 5) / 20
        }

        return data
    }

    private fun CreateWallBoundaries() {
        val bodyDef = BodyDef()
        bodyDef.position.set(0f, 0f)
        bodyDef.type = BodyDef.BodyType.StaticBody

        val groundBox = PolygonShape()
        groundBox.setAsBox(0.1f, Math.max(Gdx.graphics.width, Gdx.graphics.height).toFloat())

        walls_bodies = (1..4).map {
            world.createBody(bodyDef).apply {
                userData = eWall
                createFixture(groundBox, 0.0f)
            }
        }.toTypedArray()

        groundBox.dispose()
    }

    private fun CreateSheepBody() {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(0f, 0f)

        sheep_body = SteerableBody(world.createBody(bodyDef))

        val circleShape = CircleShape()
        circleShape.radius = SHEEP_SIZE / 2

        val fixtureDef = FixtureDef()
        fixtureDef.shape = circleShape
        fixtureDef.density = 0.1f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f

        sheep_body.body.createFixture(fixtureDef)
        sheep_body.body.userData = eSheep

        circleShape.dispose()
    }

    private fun CreateHomeBody() {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(0f, 0f)

        home_body = SteerableBody(world.createBody(bodyDef))

        val shape = PolygonShape()
        shape.setAsBox(HOME_SIZE / 2, HOME_SIZE / 2)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 0.1f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f

        home_body.body.createFixture(fixtureDef)
        home_body.body.userData = eHome

        shape.dispose()
    }

    //Touchpad touch
    private fun handleTouch() {
        val vec = Vector2(touchpad.knobPercentX, touchpad.knobPercentY).nor()
        vec.x *= SHEEP_SPEED
        vec.y *= SHEEP_SPEED

        sheep_body.body.setLinearVelocity(vec.x, vec.y)

        //set Angle
        sheep_body.body.setTransform(sheep_body.position, Math.toRadians(calculateAngle(vec.x, vec.y).toDouble()).toFloat())
    }

    //Finger touch
    private fun handleTouch(x: Int, y: Int) {

        var targetx = x.toFloat()
        var targety = (Gdx.graphics.height - y).toFloat()

        targetx = targetx / Gdx.graphics.width.toFloat() * CAMERA_WIDTH - CAMERA_WIDTH / 2f
        targety = targety / Gdx.graphics.height.toFloat() * CAMERA_HEIGHT - CAMERA_HEIGHT / 2f

        val pos = sheep_body.position
        targetx -= pos.x
        targety -= pos.y

        val divider = Math.max(Math.abs(targetx), Math.abs(targety))

        targetx = targetx / divider * SHEEP_SPEED
        targety = targety / divider * SHEEP_SPEED

        sheep_body.body.setLinearVelocity(targetx, targety)

        //set Angle
        sheep_body.body.setTransform(pos, Math.toRadians(calculateAngle(targetx, targety).toDouble()).toFloat())
    }

    private fun StartWolves() {
        for (b in wolf_bodies) {
            setRandomMovement(b.body, WILD_WOLF_SPEED)
        }
    }

    override fun beginContact(contact: Contact) {
        val bodyA = contact.fixtureA.body
        val bodyB = contact.fixtureB.body

        val typeA = bodyA.userData as GameObjectType?
        val typeB = bodyB.userData as GameObjectType?

        if (typeA != null && typeB != null) {

            when (typeA) {
                eHome -> when (typeB) {
                    eWildWolf -> setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED)
                    eSheep -> gameState = State.eNextLevel
                    else -> {}
                }
                eWall -> if (typeB === eWildWolf) {
                    setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED)
                }
                eWildWolf -> {
                    setRandomMovement(bodyA, GameData.WILD_WOLF_SPEED)

                    if (typeB === eWildWolf) {
                        setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED)
                    } else if (typeB === eSheep) {
                        gameState = State.eGameOver_Wild
                    }
                }
                eHungryWolf -> if (typeB === eWildWolf) {
                    setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED)
                } else if (typeB === eSheep) {
                    gameState = State.eGameOver_Hungry
                }
                eAlphaWolf -> if (typeB === eWildWolf) {
                    setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED)
                } else if (typeB === eSheep) {
                    gameState = State.eGameOver_Alpha
                }
                eSheep -> when (typeB) {
                    eWildWolf -> gameState = State.eGameOver_Wild
                    eHungryWolf -> gameState = State.eGameOver_Hungry
                    eAlphaWolf -> gameState = State.eGameOver_Alpha
                    eHome -> gameState = State.eNextLevel
                    else -> {}
                }
            }
        }
    }

    private fun gameOver(dialogType: GameDialogType) {
        (Gdx.app.applicationListener as Game).screen = GameplayDialog(dialogType)
    }

    private fun nextLevel() {
        (Gdx.app.applicationListener as Game).screen = GameplayDialog(GameDialogType.typeSheepSucceeded)
    }

    override fun endContact(contact: Contact) {

    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {

    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {

    }


    private inner class WolvesData {
        internal var WildWolves: Int = 0
        internal var HungryWolves: Int = 0
        internal var AlphaWolves: Int = 0

        internal val wolvesCount: Int
            get() = WildWolves + HungryWolves + AlphaWolves
    }

    companion object {

        //Physics
        lateinit var world: World
        lateinit var debugRenderer: Box2DDebugRenderer
        lateinit var camera: OrthographicCamera

        lateinit var sheep_body: SteerableBody
        lateinit var wolf_bodies: Array<SteerableBody>
        lateinit var walls_bodies: Array<Body>
        lateinit var home_body: SteerableBody
    }

}


