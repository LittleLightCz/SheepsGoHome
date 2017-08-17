package com.sheepsgohome.dialogs

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin

class NewBadgeDialog(badgeName: String, badgeTexture: Texture) : AbstractFixedSizeDialog() {

    private val BUTTON_WIDTH = 50f
    private val BADGE_SIZE = 100f

    private val buttonOk: TextButton = TextButton(loc.get("ok"), skin).apply {
        style.font.setScale(0.5f)
    }

    private val titleLabel: Label = Label(loc.get("new.badge.earned"), skin).apply {
        setFontScale(0.45f)
    }

    private val nameLabel: Label = Label(badgeName, skin)

    init {
        fixedHeight = 140f

        contentTable.add(titleLabel).row()

        contentTable.add(Image(badgeTexture))
                .size(BADGE_SIZE, BADGE_SIZE)
                .row()

        contentTable.add(nameLabel).row()

        buttonOk.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })

        contentTable.add(buttonOk)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .row()
    }

}
