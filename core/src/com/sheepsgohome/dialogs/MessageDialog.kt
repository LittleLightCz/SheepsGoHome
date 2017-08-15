package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

import com.sheepsgohome.GameData.Loc

class MessageDialog(val message: String, skin: Skin, windowStyleName: String) : Dialog("", skin, windowStyleName) {

    private val BUTTON_WIDTH = 50f
    private val WINDOW_SIZE_MULTIPLICATOR = 1.70f
    private val messageLabel: Label = Label(message, skin)
    private val buttonCancel: TextButton

    private var prefHeight = 70f
    private var cancelAction: CancelAction? = null

    init {
        messageLabel.setWrap(true)
        contentTable.add(messageLabel)
                .expand()
                .width(120f)
                .center()
                .padTop(5f)
                .row()

        buttonCancel = TextButton(Loc.get("cancel"), skin)
        buttonCancel.style.font.setScale(0.5f)
        buttonCancel.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                cancelAction?.CancelPressed()
                hide()
            }
        })

        initialize()
    }

    private fun initialize() {
        isModal = true
        isMovable = false
        isResizable = false
    }

    override fun result(`object`: Any?) {
        this.hide()
    }


    override fun getPrefWidth(): Float {
        return 90 * WINDOW_SIZE_MULTIPLICATOR
    }

    override fun getPrefHeight(): Float {
        return prefHeight * WINDOW_SIZE_MULTIPLICATOR
    }

    fun setPrefHeight(height: Float) {
        prefHeight = height
    }

    fun addCancelButton(action: CancelAction) {
        cancelAction = action
        contentTable.add(buttonCancel).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
    }

    interface CancelAction {
        fun CancelPressed()
    }
}
