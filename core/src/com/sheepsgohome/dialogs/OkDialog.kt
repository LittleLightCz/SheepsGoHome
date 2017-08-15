package com.sheepsgohome.dialogs

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.GameData.Loc
import com.sheepsgohome.screens.SettingsPlayerScreen

class OkDialog(message: String, skin: Skin, windowStyleName: String) : Dialog("", skin, windowStyleName) {

    private val BUTTON_WIDTH = 50f
    private val WINDOW_SIZE_MULTIPLICATOR = 1.70f
    private val buttonOk: TextButton = TextButton(Loc.get("ok"), skin)
    private val message = Label(message, skin)

    private var prefHeight = 70f
    private var screen: SettingsPlayerScreen? = null

    init {
        this.message.setWrap(true)
        contentTable.add(this.message).expand().width(120f).center().padTop(5f).row()

        buttonOk.style.font.setScale(0.5f)
        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                if (screen != null) {
                    (Gdx.app.applicationListener as Game).screen = screen
                }
            }
        })

        contentTable.add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).bottom().padBottom(5f).row()

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

    fun setRedirectScreen(screen: SettingsPlayerScreen) {
        this.screen = screen
    }
}
