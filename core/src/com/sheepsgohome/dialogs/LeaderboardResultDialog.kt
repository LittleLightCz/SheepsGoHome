package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class LeaderboardResultDialog(rank: Long) : AbstractFixedSizeDialog() {

    private val buttonOk = SmallSheepButton(Loc.ok).apply {
        style.font.setScale(0.5f)
        onClick { hide() }
    }

    private val rankLabel = Label("$rank.", skin, "menuTitle").apply {
        setFontScale(0.5f)
    }

    private val titleLabel = Label(Loc.yourPositionIs, skin).apply {
        setFontScale(0.40f)
    }

    init {

        contentTable.add(titleLabel)
                .width(130f)
                .padTop(10f)
                .row()

        if (rank == -1L) {
            titleLabel.setText(Loc.yourRankIsNotPublic)
            titleLabel.setWrap(true)

            fixedHeight = 100f
        } else {
            titleLabel.setAlignment(Align.center)

            contentTable.add(rankLabel)
                    .center()
                    .row()

            fixedHeight = 70f
        }

        buttonOk.addTo(contentTable)
                .bottom()
                .padBottom(10f)
                .row()
    }
}
