package com.sheepsgohome.controls

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.sheepsgohome.gdx.onClick

class LaserButton {

    private val SIZE = 16f
    private val OFFSET = 7f

    private val laserButtonTexture = Texture("laserButton.png")

    val image = Image(laserButtonTexture).apply {
        setBounds(OFFSET,OFFSET, SIZE, SIZE)

        addAction(Actions.alpha(0.5f))
    }

    fun onClick(action: () -> Unit) = image.onClick { action() }

    fun dispose() = laserButtonTexture.dispose()

}

fun Stage.addActor(laserButton: LaserButton) = addActor(laserButton.image)