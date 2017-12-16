package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.shared.GameSkins.skin


class OkDialog(message: String) : AbstractFixedSizeDialog() {

    private val BUTTON_WIDTH = 50f

    private val buttonOk: TextButton = TextButton(Loc.ok, skin).apply {
        style.font.setScale(0.5f)
    }

    private val messageLabel = Label(message, skin).apply {
        setWrap(true)
    }

    var onConfirm: (() -> Unit)? = null

    init {
        buttonOk.addListener(clicked {
            hide()
            onConfirm?.invoke()
        })

        contentTable.add(messageLabel)
                .expand()
                .width(120f)
                .center()
                .padTop(5f)
                .row()

        contentTable.add(buttonOk)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .bottom()
                .padBottom(5f)
                .row()
    }



}
