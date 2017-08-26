package com.sheepsgohome.ui

import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.shared.GameSkins

open class SheepButton(
        text: String,
        var buttonWidth: Float,
        var buttonHeight: Float = buttonWidth / 2
) : TextButton(text, GameSkins.skin) {

    init {
        style.font.setScale(0.5f)
    }

    fun addTo(table: Table): Cell<SheepButton> = table.add(this).size(buttonWidth, buttonHeight)
}

fun TextButton.onClick(action: () -> Unit) = addListener(clicked { action() })
