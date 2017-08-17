package com.sheepsgohome.dialogs

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.screens.SettingsPlayerScreen
import com.sheepsgohome.shared.GameData.loc

class LeaderboardResultDialog(rank: Int, skin: Skin, windowStyleName: String) : Dialog("", skin, windowStyleName) {

    private val BUTTON_WIDTH = 50f
    private val WINDOW_SIZE_MULTIPLICATOR = 1.70f
    private val buttonOk: TextButton = TextButton(loc.get("ok"), skin)
    private val title: Label = Label(loc.get("your.position.is"), skin)
    private val rank: Label = Label(rank.toString() + ".", skin, "menuTitle")

    private var prefHeight = 70f
    private var screen: SettingsPlayerScreen? = null

    init {
        title.setFontScale(0.40f)
        contentTable.add(title).center().padTop(10f).row()
        this.rank.setFontScale(0.5f)
        contentTable.add(this.rank).center().row()

        buttonOk.style.font.setScale(0.5f)
        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
                if (screen != null) {
                    (Gdx.app.applicationListener as Game).screen = screen
                }
            }
        })

        contentTable.add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).bottom().padBottom(10f).row()

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
