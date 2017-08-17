package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.sheepsgohome.shared.GameSkins.skin

open class AbstractFixedSizeDialog : Dialog("", skin, "dialog") {

    var sizeMultiplicator = 1.70f

    var fixedWidth = 90f
    var fixedHeight = 70f

    init {
        isModal = true
        isMovable = false
        isResizable = false
    }

    override fun getPrefWidth() = fixedWidth * sizeMultiplicator
    override fun getPrefHeight() = fixedHeight * sizeMultiplicator
}