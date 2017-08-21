package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

open class MeadowScreen : Screen {

    protected val stage = Stage()

    private val backGroundTexture = Texture("menu_background.png").apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    protected val backgroundImage = Image(backGroundTexture)

    init {
        with(backgroundImage) {
            setPosition(-CAMERA_WIDTH / 2, -CAMERA_HEIGHT / 2)
            zIndex = 1
            setSize(CAMERA_WIDTH, CAMERA_HEIGHT)
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act()
        stage.draw()
    }

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        backGroundTexture.dispose()
        stage.dispose()
    }

}