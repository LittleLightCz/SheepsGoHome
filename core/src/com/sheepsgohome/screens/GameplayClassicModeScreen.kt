package com.sheepsgohome.screens

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
import com.sheepsgohome.GameObject
import com.sheepsgohome.GameObject.*
import com.sheepsgohome.GameTools.calculateAngle
import com.sheepsgohome.GameTools.setRandomMovement
import com.sheepsgohome.SteerableBody
import com.sheepsgohome.SteerableHungryWolfBody
import com.sheepsgohome.enums.GameState.*
import com.sheepsgohome.gameobjects.*
import com.sheepsgohome.screens.GameResult.*
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.ALPHA_WOLF_SIZE
import com.sheepsgohome.shared.GameData.ALPHA_WOLF_SPEED
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.HUNGRY_WOLF_SIZE
import com.sheepsgohome.shared.GameData.LEVEL
import com.sheepsgohome.shared.GameData.SHEEP_SPEED
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_LEFT
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_NONE
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_RIGHT
import com.sheepsgohome.shared.GameData.WILD_WOLF_SIZE
import com.sheepsgohome.shared.GameData.WILD_WOLF_SPEED
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameMusic.ambient
import com.sheepsgohome.shared.GameScreens.switchScreen
import com.sheepsgohome.shared.GameSkins.skin
import java.util.*

class GameplayClassicModeScreen : Screen, ContactListener {
    private val fpsLogger by lazy { FPSLogger() }

    //Box2D Physics
    private val debugRenderer = Box2DDebugRenderer()
    private val world = World(Vector2(0f, 0f), true)

    private val multiplier = 1f

    private val camera: OrthographicCamera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
    private val batch = SpriteBatch()
    private var gameState = RUNNING

    private val touchpadEnabled = VIRTUAL_JOYSTICK != VIRTUAL_JOYSTICK_NONE

    private val touchpad by lazy { Touchpad(0f, skin).apply {
        val touchPadSize = 30f

        when (VIRTUAL_JOYSTICK) {
            VIRTUAL_JOYSTICK_RIGHT -> setBounds(CAMERA_WIDTH - touchPadSize, 0f, touchPadSize, touchPadSize)
            VIRTUAL_JOYSTICK_LEFT -> setBounds(0f, 0f, touchPadSize, touchPadSize)
        }

        addAction(Actions.alpha(0.5f))
    }}

    private var wolves_count = 0

    private val stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

    private val levelLabel = Label(loc.format("level", LEVEL), skin, "levelTitle").apply {
        setFontScale((CAMERA_WIDTH * multiplier - 40) / prefWidth)
        addAction(Actions.sequence(
            Actions.alpha(1f),
            Actions.fadeOut(3f)
        ))
    }

    private lateinit var wolf_texture: Texture
    private lateinit var hungry_wolf_texture: Texture
    private lateinit var alpha_wolf_texture: Texture

    private val home = Home(world)
    private val sheep = Sheep(world)
    private val walls = listOf(
            TopWall(world),
            BottomWall(world),
            LeftWall(world),
            RightWall(world)
    )

    private lateinit var background_texture: Texture

    private lateinit var wolves: List<Sprite>


    private lateinit var background: Sprite

    init {
        Box2D.init()
        world.setContactListener(this)
    }

    override fun show() {
        //catch menu and back buttons
        Gdx.input.isCatchBackKey = true
        Gdx.input.setCatchMenuKey(true)

        //Ambient
        if (SOUND_ENABLED && !ambient.isPlaying) {
            ambient.play()
        }

        val table = Table().apply {
            add(levelLabel)
            setFillParent(true)
        }

        stage.addActor(table)

        if (touchpadEnabled) {
            stage.addActor(touchpad)
        }

        Gdx.input.inputProcessor = stage

        prepareGameObjects()

        StartWolves()
    }

    private fun prepareGameObjects() {


        wolf_texture = Texture("wolf.png")
        hungry_wolf_texture = Texture("wolf-hungry.png")
        alpha_wolf_texture = Texture("wolf-alpha.png")

        background_texture = Texture("grass-background.jpg")
        background_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        background = Sprite(background_texture)
        background.setPosition(-CAMERA_WIDTH, -CAMERA_HEIGHT)
        background.setSize(CAMERA_WIDTH * 2, CAMERA_HEIGHT * 2)

        wolves_count = CreateWolfBodies()

        wolves = wolf_bodies.map { it.userData as GameObject }
                .map { wolfType ->
                    when (wolfType) {
                        HUNGRY_WOLF -> Sprite(hungry_wolf_texture).apply {
                            setSize(HUNGRY_WOLF_SIZE, HUNGRY_WOLF_SIZE)
                        }
                        ALPHA_WOLF -> Sprite(alpha_wolf_texture).apply {
                            setSize(ALPHA_WOLF_SIZE, ALPHA_WOLF_SIZE)
                        }
                        else -> Sprite(wolf_texture).apply {
                            setSize(WILD_WOLF_SIZE, WILD_WOLF_SIZE)
                        }
                    }.apply { setOriginCenter() }
                }
    }


    override fun render(delta: Float) {
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        when (gameState) {
            RUNNING -> renderGameScene()
            GAME_OVER_BY_WILD_WOLF -> gameOver(SHEEP_EATEN_BY_WILD_WOLF)
            GAME_OVER_BY_HUNGRY_WOLF -> gameOver(SHEEP_EATEN_BY_HUNGRY_WOLF)
            GAME_OVER_BY_ALPHA_WOLF -> gameOver(SHEEP_EATEN_BY_ALPHA_WOLF)
            NEXT_LEVEL -> nextLevel()
        }

        //fps debug
        //        fpsLogger.log();
    }

    private fun renderGameScene() {
        //positioning
        sheep.updateSprite()

        //input
        if (touchpadEnabled) {
            if (touchpad.isTouched) {
                handleTouch()
            } else {
                sheep.nullifyVelocity()
            }
        } else {
            if (Gdx.input.isTouched) {
                handleTouch(Gdx.input.x, Gdx.input.y)
            } else {
                sheep.nullifyVelocity()
            }
        }

        //drawing
        batch.projectionMatrix = camera.combined
        batch.begin()

        background.draw(batch)

        sheep.draw(batch)

        //draw home (top center)
        home.draw(batch)

        //draw wolves
        var wolf: GameObject
        for (i in 0..wolves_count - 1) {
            wolf = wolf_bodies[i].userData as GameObject

            when (wolf) {
                ALPHA_WOLF -> {
                    wolves[i].setPosition(
                            wolf_bodies[i].position.x - ALPHA_WOLF_SIZE / 2,
                            wolf_bodies[i].position.y - ALPHA_WOLF_SIZE / 2
                    )

                    setAlphaWolfVelocity(wolf_bodies[i].body)
                }
                HUNGRY_WOLF -> {
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

//        fpsLogger.log()
//        debugRenderer.render(world, camera.combined);

        world.step(graphics.deltaTime, 6, 2)

        stage.act()
        stage.draw()
    }

    private fun setAlphaWolfVelocity(wolf: Body) {
        val sheepPosition = sheep.steerableBody.position

        val vec = Vector2(sheepPosition).sub(wolf.position).nor()
        val alphaSpeed = ALPHA_WOLF_SPEED / Math.abs(wolf.position.dst(sheepPosition) / CAMERA_HEIGHT)

        wolf.setLinearVelocity(vec.x * alphaSpeed, vec.y * alphaSpeed)
    }

    override fun resize(width: Int, height: Int) {

        stage.viewport.update(width, height, true)

        //world boundaries
        walls.forEach { it.updatePosition() }

        //sheep
        sheep.positionBottomCenter()

        //home
        home.positionTopCenter()

        //wolves
        val gap = 0.5f
        val start_offset = 6f

        var x = start_offset + -CAMERA_WIDTH / 2
        var y = CAMERA_HEIGHT / 2 - Home.HOME_SIZE - start_offset

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
        if (ambient.isPlaying) {
            ambient.pause()
        }

        //release menu and back buttons
        Gdx.input.isCatchBackKey = false
        Gdx.input.setCatchMenuKey(false)

        dispose()
    }

    override fun dispose() {
        sheep.dispose()
        wolf_texture.dispose()
        hungry_wolf_texture.dispose()
        alpha_wolf_texture.dispose()
        home.dispose()
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

        return CreateWolfBody(WILD_WOLF, bodyDef, fixtureDef)
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

        return CreateWolfBody(HUNGRY_WOLF, bodyDef, fixtureDef)
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

        return CreateWolfBody(ALPHA_WOLF, bodyDef, fixtureDef)
    }

    private fun CreateWolfBody(wolf: GameObject, bodyDef: BodyDef, fixtureDef: FixtureDef): Body {

        val body = world.createBody(bodyDef)
        body.userData = wolf
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
            wolf_bodies_list.add(SteerableHungryWolfBody(CreateHungryWolfBody(), sheep))
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



    //Touchpad touch
    private fun handleTouch() {
        val vec = Vector2(touchpad.knobPercentX, touchpad.knobPercentY).nor()
        vec.x *= SHEEP_SPEED
        vec.y *= SHEEP_SPEED

        sheep.updateVelocity(vec)
    }

    //Finger touch
    private fun handleTouch(x: Int, y: Int) {

        var targetx = x.toFloat()
        var targety = (Gdx.graphics.height - y).toFloat()

        targetx = targetx / Gdx.graphics.width.toFloat() * CAMERA_WIDTH - CAMERA_WIDTH / 2f
        targety = targety / Gdx.graphics.height.toFloat() * CAMERA_HEIGHT - CAMERA_HEIGHT / 2f

        val pos = sheep.steerableBody.position
        targetx -= pos.x
        targety -= pos.y

        val divider = Math.max(Math.abs(targetx), Math.abs(targety))

        targetx = targetx / divider * SHEEP_SPEED
        targety = targety / divider * SHEEP_SPEED

        sheep.updateVelocity(targetx, targety)
    }

    private fun StartWolves() {
        for (b in wolf_bodies) {
            setRandomMovement(b.body, WILD_WOLF_SPEED)
        }
    }

    private fun handleContact(bodyA: Body, bodyB: Body) {
        val objA = bodyA.userData
        val objB = bodyB.userData

        when (objA) {
            WILD_WOLF -> setRandomMovement(bodyA, GameData.WILD_WOLF_SPEED)
            is Sheep -> when (objB) {
                WILD_WOLF -> gameState = GAME_OVER_BY_WILD_WOLF
                HUNGRY_WOLF -> gameState = GAME_OVER_BY_HUNGRY_WOLF
                ALPHA_WOLF -> gameState = GAME_OVER_BY_ALPHA_WOLF
                is Home -> gameState = NEXT_LEVEL
            }
        }
    }

    override fun beginContact(contact: Contact) {
        val bodyA = contact.fixtureA.body
        val bodyB = contact.fixtureB.body

        // Both combinations - reducing quadratic complexity to linear
        handleContact(bodyA, bodyB)
        handleContact(bodyB, bodyA)
    }

    private fun gameOver(result: GameResult) = switchScreen(GameClassicModeResultScreen(result))

    private fun nextLevel() = switchScreen(GameClassicModeResultScreen(SHEEP_SUCCEEDED))

    override fun endContact(contact: Contact) {}

    override fun preSolve(contact: Contact, oldManifold: Manifold) {}

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {}

    private inner class WolvesData {
        internal var WildWolves = 0
        internal var HungryWolves = 0
        internal var AlphaWolves = 0

        internal val wolvesCount
            get() = WildWolves + HungryWolves + AlphaWolves
    }

    companion object {
        lateinit var wolf_bodies: Array<SteerableBody>
    }

}


