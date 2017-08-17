package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

import com.sheepsgohome.shared.GameData.Loc


class ContinueLastGameDialog(title: String, skin: Skin, windowStyleName: String) : Dialog(title, skin, windowStyleName) {
    private val buttonNo = TextButton(Loc.get("no"), skin)
    private val buttonYes = TextButton(Loc.get("yes"), skin)
    private val label = Label(Loc.get("continue.last.game"), skin)

    init {
        label.setFontScale(0.45f)
        contentTable.add(label).padTop(15f)

        buttonYes.style.font.setScale(0.5f)

        buttonTable.add(buttonYes).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
        buttonTable.add(buttonNo).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)

        setObject(buttonYes, true)
        setObject(buttonNo, false)

        initialize()
    }

    private fun initialize() {
        buttonTable.padBottom(20f)
        isModal = true
        isMovable = false
        isResizable = false
    }

    override fun result(`object`: Any?) {}

    override fun getPrefWidth(): Float {
        // force dialog width
        return 150f
    }

    override fun getPrefHeight(): Float {
        // force dialog height
        return 100f
    }

    companion object {

        private val BUTTON_WIDTH = 50f
    }

}
