package com.sheepsgohome.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.controls.LaserButton
import com.sheepsgohome.controls.addActor
import com.sheepsgohome.dataholders.WolvesData
import com.sheepsgohome.enums.GameState.*
import com.sheepsgohome.gameobjects.*
import com.sheepsgohome.gameobjects.Home.Companion.HOME_SIZE
import com.sheepsgohome.gameobjects.bullets.LaserBeam
import com.sheepsgohome.gameobjects.bullets.LaserBeamManager
import com.sheepsgohome.gameobjects.walls.BottomWall
import com.sheepsgohome.gameobjects.walls.LeftWall
import com.sheepsgohome.gameobjects.walls.RightWall
import com.sheepsgohome.gameobjects.walls.TopWall
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.positioning.BodyPositioner
import com.sheepsgohome.screens.gameresult.LasersModeResultScreen
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.LEVEL
import com.sheepsgohome.shared.GameData.LEVEL_LASERS
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_LEFT
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_NONE
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK_RIGHT
import com.sheepsgohome.shared.GameMusic.ambient
import com.sheepsgohome.shared.GameSkins.skin

class GameplayLasersModeScreen : Screen, ContactListener {
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

    private val stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

    private val levelLabel = Label(Loc.level(LEVEL_LASERS), skin, "levelTitle").apply {
        setFontScale((CAMERA_WIDTH * multiplier - 40) / prefWidth)
        addAction(Actions.sequence(
            Actions.alpha(1f),
            Actions.fadeOut(3f)
        ))
    }

    private val grass = Grass().apply {
        translateY(CAMERA_HEIGHT / 2)
    }

    private val home = Home(world)
    private val sheep = SheepWithLaser(world)

    private val topWall = TopWall(world)

    private val walls = listOf(
            topWall,
            BottomWall(world),
            LeftWall(world),
            RightWall(world)
    )

    private val wolves: List<Wolf>

    private val laserButton = LaserButton().apply {
        onClick {
            sheep.shoot()
        }
    }

    init {
        Box2D.init()
        world.setContactListener(this)

        val data = getWolvesData(LEVEL)

//        wolves = mutableListOf(
//            List(data.wildWolvesCount) { WildWolf(world) },
//            List(data.hungryWolvesCount) { HungryWolf(world, sheep) },
//            List(data.alphaWolvesCount) { AlphaWolf(world, sheep) }
//        ).flatten()
//
//        Collections.shuffle(wolves)

        wolves = emptyList()
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
        stage.addActor(laserButton)

        if (touchpadEnabled) {
            stage.addActor(touchpad)
        }

        Gdx.input.inputProcessor = stage

        kickOffWildWolves()
    }

    override fun render(delta: Float) {
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        when (gameState) {
            RUNNING -> renderGameScene()
            else -> handleGameOver()
        }
    }

    private fun renderGameScene() {
        LaserBeamManager.cleanDisposed()
        LaserBeamManager.updateSprites()

        //positioning
        sheep.updateSprite()
        camera.followVertically(sheep, 0f, CAMERA_HEIGHT)

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

        grass.draw(batch)
        sheep.draw(batch)

        //draw home (top center)
        home.draw(batch)

        //draw wolves
//        for (wolf in wolves) {
//            when (wolf) {
//                is AlphaWolf -> {
//                    wolf.updateVelocity()
//                    wolf.updateSprite()
//                }
//                is HungryWolf -> {
//                    wolf.calculateSteeringBehaviour()
//                    wolf.updateSprite()
//                }
//                else -> wolf.updateSprite()
//            }
//
//            wolf.draw(batch)
//        }

        LaserBeamManager.drawLaserBeams(batch)

        batch.end()

//        fpsLogger.log()
//        debugRenderer.render(world, camera.combined)

        world.step(graphics.deltaTime, 6, 2)

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {

        stage.viewport.update(width, height, true)

        //world boundaries
        walls.forEach { it.setDefaultPosition() }

        //move the top wall up
        topWall.transform(y = CAMERA_HEIGHT * 1.5f, angle = (Math.PI / 2f).toFloat())

        //sheep
        sheep.positionBottomCenter()

        //home
        home.positionTopCenter()
        home.transform(0f, (CAMERA_HEIGHT * 1.5f) - (HOME_SIZE / 2), 0f)

        //wolves positioning
        val wolfBodies = wolves.map { it.body }
        BodyPositioner().alignInCenteredGrid(
                wolfBodies,
                maxBodiesInRow = 13,
                columnSize = WildWolf.WILD_WOLF_SIZE + 0.5f,
                verticalOffset = (CAMERA_HEIGHT / 2) - Home.HOME_SIZE - 5
        )

    }

    override fun pause() {}

    override fun resume() {}

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
        LaserBeamManager.cleanup()
        laserButton.dispose()

        grass.dispose()
        home.dispose()
        sheep.dispose()
        wolves.forEach { it.dispose() }

        batch.dispose()
        world.dispose()
        stage.dispose()
    }

    private fun getWolvesData(level: Int): WolvesData {
        val data = WolvesData()

        data.wildWolvesCount = ((level - 1) % 5 + 1) * 5

        if (level < 6) {
            data.hungryWolvesCount = 0
        } else {
            data.hungryWolvesCount = (level - 1 - 5) / 5 % 4 + 1
        }

        if (level < 26) {
            data.alphaWolvesCount = 0
        } else {
            data.alphaWolvesCount = (level - 1 - 5) / 20
        }

        return data
    }

    //Touchpad touch
    private fun handleTouch() {
        val vec = Vector2(touchpad.knobPercentX, touchpad.knobPercentY).nor()
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

        targetx /= divider
        targety /= divider

        sheep.updateVelocity(targetx, targety)
    }

    private fun kickOffWildWolves() {
        wolves.filterIsInstance<WildWolf>().forEach { it.setRandomMovement() }
    }

    private fun handleGameOver() {
        switchScreen(LasersModeResultScreen(gameState))
    }

    /**
     * Contact handling
     */

    override fun beginContact(contact: Contact) {
        val bodyA = contact.fixtureA.body
        val bodyB = contact.fixtureB.body

        // Both combinations - reducing quadratic complexity to linear
        handleContact(bodyA, bodyB)
        handleContact(bodyB, bodyA)
    }

    override fun endContact(contact: Contact) {}

    override fun preSolve(contact: Contact, oldManifold: Manifold) {}

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {}

    private fun handleContact(bodyA: Body, bodyB: Body) {
        val objA = bodyA.userData
        val objB = bodyB.userData

        when (objA) {
            is LaserBeam -> objA.handleCollision()
            is WildWolf -> objA.setRandomMovement()
            is SheepWithLaser -> when (objB) {
                is WildWolf -> gameState = GAME_OVER_BY_WILD_WOLF
                is HungryWolf -> gameState = GAME_OVER_BY_HUNGRY_WOLF
                is AlphaWolf -> gameState = GAME_OVER_BY_ALPHA_WOLF
                is Home -> gameState = NEXT_LEVEL
                is LaserBeam -> {
                    objA.hitByLaserBeam()
                    if (objA.lives < 1) gameState = GAME_OVER_BY_LASER_BURN
                }
            }
        }
    }
}

private fun OrthographicCamera.followVertically(sheep: Sheep, bottomLimit: Float, upperLimit: Float) {
    if (sheep.yPosition in bottomLimit..upperLimit) {
        position.y = sheep.yPosition
        update()
    }
}

