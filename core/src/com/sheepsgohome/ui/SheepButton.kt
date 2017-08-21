package com.sheepsgohome.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.gdx.clicked
import com.sheepsgohome.shared.GameSkins.skin

class SheepButton(text: String): TextButton(text, skin) {

    companion object {
        val BUTTON_WIDTH = 100f
    }

    init {
        style.font.setScale(0.5f)
    }

    fun onClick(action: () -> Unit) = addListener(clicked { action() })

    fun addTo(table: Table): Table {
        table.add(this).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
        return table
    }

}