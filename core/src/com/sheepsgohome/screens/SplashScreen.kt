package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sheepsgohome.GameScreens
import com.sheepsgohome.GameSkins.skin
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.Loc

class SplashScreen : Screen {

    private val SHEEP_SPLASH_SIZE = 0.18f
    private val splashImage: Image

    private val stage = Stage()
    private val texture = Texture("menu_background.png")
    private val title = Label(Loc.get("sheeps.go.home"), skin, "menuTitle")

    private val camera: OrthographicCamera
    private val sheep: Texture
    private val sheepImage: Image

    init {
        texture.setFilter(Linear, Linear)
        splashImage = Image(texture)

        sheep = Texture("sheep_success.png")
        sheep.setFilter(Linear, Linear)
        sheepImage = Image(sheep)

        camera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
    }

    override fun show() {
        splashImage.setPosition(-GameData.CAMERA_WIDTH / 2, -GameData.CAMERA_HEIGHT / 2)
        splashImage.zIndex = 10
        splashImage.setSize(GameData.CAMERA_WIDTH, GameData.CAMERA_HEIGHT)
        splashImage.addAction(Actions.sequence(
                Actions.alpha(0f),
                Actions.fadeIn(2.5f),
                Actions.delay(2.5f),
                Actions.fadeOut(1.4f)
        ))


        sheepImage.setPosition(50f, -130f)
        sheepImage.zIndex = 100
        sheepImage.setSize(sheepImage.width * SHEEP_SPLASH_SIZE, sheepImage.height * SHEEP_SPLASH_SIZE)
        sheepImage.addAction(Actions.sequence(
                Actions.delay(2.5f),
                Actions.moveTo(-20f, sheepImage.y, 0.5f),
                Actions.delay(2f),
                Actions.fadeOut(1f),
                Actions.run { (Gdx.app.applicationListener as Game).screen = GameScreens.mainMenuScreen }
        ))

        val multiplier = 1f
        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)
        title.setPosition((CAMERA_WIDTH - title.prefWidth) / 2f - CAMERA_WIDTH / 2f, 60f)
        title.addAction(Actions.sequence(
                Actions.delay(2.5f),
                Actions.moveTo(title.x, 20f, 0.5f)
        ))

        stage.addActor(splashImage)
        stage.addActor(sheepImage)
        stage.addActor(title)

        stage.viewport.camera = camera
    }

    override fun render(delta: Float) {
        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        texture.dispose()
        sheep.dispose()
        stage.dispose()
    }
}
