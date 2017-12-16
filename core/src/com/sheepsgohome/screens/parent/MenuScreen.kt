package com.sheepsgohome.screens.parent

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

open class MenuScreen : MeadowScreen(Stage(StretchViewport(
        CAMERA_WIDTH * multiplier,
        CAMERA_HEIGHT * multiplier
))) {

    companion object {
        const val multiplier = 2f
    }

    protected val table = Table().apply {
        setFillParent(true)
    }

    init {
        backgroundImage.width = CAMERA_WIDTH * multiplier
        backgroundImage.height = CAMERA_HEIGHT * multiplier
        backgroundImage.setPosition(0f, 0f)

        stage.addActor(table)

        Gdx.input.inputProcessor = stage
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, false)
    }
}