package com.sheepsgohome.dialogs

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin


class OkDialog(message: String) : AbstractFixedSizeDialog() {

    private val BUTTON_WIDTH = 50f

    private val buttonOk: TextButton = TextButton(loc.get("ok"), skin).apply {
        style.font.setScale(0.5f)
    }

    private val messageLabel = Label(message, skin).apply {
        setWrap(true)
    }

    var redirectScreen: Screen? = null

    init {
        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                GameScreens.switchScreen(redirectScreen)
            }
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
