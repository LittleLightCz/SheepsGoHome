package com.sheepsgohome.dialogs

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.GameData.Loc

class NewBadgeDialog(
        badgeName: String,
        badgeTexture: Texture,
        skin: Skin,
        windowStyleName: String
) : Dialog("", skin, windowStyleName) {

    private val BUTTON_WIDTH = 50f
    private val WINDOW_SIZE_MULTIPLICATOR = 1.70f
    private val BADGE_SIZE = 100f
    private val buttonOk: TextButton = TextButton(Loc.get("ok"), skin)
    private val title: Label = Label(Loc.get("new.badge.earned"), skin)
    private val name: Label = Label(badgeName, skin)

    init {

        title.setFontScale(0.45f)
        contentTable.add(title).row()
        contentTable.add(Image(badgeTexture)).size(BADGE_SIZE, BADGE_SIZE).row()
        contentTable.add(name).row()

        buttonOk.style.font.setScale(0.5f)
        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        contentTable.add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()

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
        // force dialog width
        return 90 * WINDOW_SIZE_MULTIPLICATOR
    }

    override fun getPrefHeight(): Float {
        // force dialog height
        return 140 * WINDOW_SIZE_MULTIPLICATOR
    }

}
