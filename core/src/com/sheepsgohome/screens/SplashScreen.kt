package com.sheepsgohome.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MeadowScreen
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameSkins.skin

class SplashScreen : MeadowScreen() {

    private val SHEEP_SPLASH_SIZE = 0.18f

    private val camera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)

    private val titleLabel = Label(Loc.sheepsGoHome, skin, "menuTitle")

    private val sheepTexture = Texture("sheep_success.png").apply {
        setFilter(Linear, Linear)
    }

    private val sheepImage = Image(sheepTexture)

    init {
        with(backgroundImage) {
            addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.fadeIn(2.5f),
                    Actions.delay(2.5f),
                    Actions.fadeOut(1.4f)
            ))
        }

        with(sheepImage) {
            setPosition(50f, -130f)
            zIndex = 2
            setSize(width * SHEEP_SPLASH_SIZE, height * SHEEP_SPLASH_SIZE)
            addAction(Actions.sequence(
                    Actions.delay(2.5f),
                    Actions.moveTo(-20f, y, 0.5f),
                    Actions.delay(2f),
                    Actions.fadeOut(1f),
                    Actions.run { switchToMainMenuScreen() }
            ))
        }


        val multiplier = 1f

        with(titleLabel) {
            val fontScale = (CAMERA_WIDTH * multiplier - 20) / titleLabel.prefWidth

            setFontScale(fontScale)
            setPosition((CAMERA_WIDTH - prefWidth) / 2f - CAMERA_WIDTH / 2f, 60f)
            addAction(Actions.sequence(
                    Actions.delay(2.5f),
                    Actions.moveTo(x, 20f, 0.5f)
            ))
        }

        with(stage) {
            addActor(sheepImage)
            addActor(titleLabel)
        }

        stage.viewport.camera = camera
    }

    override fun dispose() {
        super.dispose()
        sheepTexture.dispose()
    }
}
