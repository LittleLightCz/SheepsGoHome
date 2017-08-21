package com.sheepsgohome.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.shared.GameSkins

open class SheepButton(text: String, private val buttonWidth: Float) : TextButton(text, GameSkins.skin) {

    init {
        style.font.setScale(0.5f)
    }

    fun onClick(action: () -> Unit) = addListener(clicked { action() })

    fun addTo(table: Table): Table {
        table.add(this).size(buttonWidth, buttonWidth / 2)
        return table
    }

}