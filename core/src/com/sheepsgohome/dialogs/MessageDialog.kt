package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin

class MessageDialog(message: String) : AbstractFixedSizeDialog() {

    private val BUTTON_WIDTH = 50f

    private val messageLabel: Label = Label(message, skin).apply {
        setWrap(true)
    }

    private val buttonCancel = TextButton(loc.get("cancel"), skin).apply {
        style.font.setScale(0.5f)
    }

    init {
        contentTable.add(messageLabel)
                .expand()
                .width(120f)
                .center()
                .padTop(5f)
                .row()
    }

    fun addCancelButton(action: () -> Unit) {

        buttonCancel.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                action()
                hide()
            }
        })

        contentTable.add(buttonCancel)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .row()
    }

}
