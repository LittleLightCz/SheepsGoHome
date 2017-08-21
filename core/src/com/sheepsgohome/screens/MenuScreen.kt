package com.sheepsgohome.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

open class MenuScreen : MeadowScreen() {

    protected val multiplier = 2f

    protected val table = Table().apply {
        setFillParent(true)
    }

    init {
        backgroundImage.width = CAMERA_WIDTH * multiplier
        backgroundImage.height = CAMERA_HEIGHT * multiplier

        stage.addActor(table)
    }

    override fun resize(width: Int, height: Int) {
//        stage.viewport.update(width, height, false)
    }
}