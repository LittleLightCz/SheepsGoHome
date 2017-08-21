package com.sheepsgohome.screens

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
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin

class SplashScreen : Screen {

    private val SHEEP_SPLASH_SIZE = 0.18f

    private val camera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
    private val stage = Stage()

    private val title = Label(loc.get("sheeps.go.home"), skin, "menuTitle")

    private val texture = Texture("menu_background.png").apply {
        setFilter(Linear, Linear)
    }

    private val splashImage = Image(texture)

    private val sheepTexture = Texture("sheep_success.png").apply {
        setFilter(Linear, Linear)
    }

    private val sheepImage = Image(sheepTexture)

    init {
        with(splashImage) {
            setPosition(-CAMERA_WIDTH / 2, -CAMERA_HEIGHT / 2)
            zIndex = 10
            setSize(CAMERA_WIDTH, CAMERA_HEIGHT)
            addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.fadeIn(2.5f),
                    Actions.delay(2.5f),
                    Actions.fadeOut(1.4f)
            ))
        }

        with(sheepImage) {
            setPosition(50f, -130f)
            zIndex = 100
            setSize(width * SHEEP_SPLASH_SIZE, height * SHEEP_SPLASH_SIZE)
            addAction(Actions.sequence(
                    Actions.delay(2.5f),
                    Actions.moveTo(-20f, y, 0.5f),
                    Actions.delay(2f),
                    Actions.fadeOut(1f),
                    Actions.run { GameScreens.switchScreen(GameScreens.mainMenuScreen) }
            ))
        }


        val multiplier = 1f

        with(title) {
            val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth

            setFontScale(fontScale)
            setPosition((CAMERA_WIDTH - prefWidth) / 2f - CAMERA_WIDTH / 2f, 60f)
            addAction(Actions.sequence(
                    Actions.delay(2.5f),
                    Actions.moveTo(x, 20f, 0.5f)
            ))
        }

        with(stage) {
            addActor(splashImage)
            addActor(sheepImage)
            addActor(title)
        }

        stage.viewport.camera = camera

    }

    override fun show() {}

    override fun render(delta: Float) {
        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        texture.dispose()
        sheepTexture.dispose()
        stage.dispose()
    }
}
