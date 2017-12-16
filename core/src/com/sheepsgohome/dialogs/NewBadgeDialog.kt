package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.badges.Badge
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.shared.GameSkins.skin

class NewBadgeDialog(badgeInstance: Badge, isNew: Boolean = true) : AbstractFixedSizeDialog() {

    private val badge = badgeInstance.clone()

    private val BUTTON_WIDTH = 50f
    private val BADGE_SIZE = 100f

    private val buttonOk: TextButton = TextButton(Loc.ok, skin).apply {
        style.font.setScale(0.5f)
    }

    private val titleLabel: Label = Label(Loc.newBadgeEarned, skin).apply {
        setFontScale(0.45f)
    }

    private val nameLabel: Label = Label(badge.badgeName, skin)

    init {
        fixedHeight = 140f

        if (isNew) {
            contentTable.add(titleLabel).row()
        }

        contentTable.add(badge.image)
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
