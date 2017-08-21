package com.sheepsgohome.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.screens.PlayerSettingsScreen
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin

class LeaderboardResultDialog(rank: Int) : AbstractFixedSizeDialog() {

    private val BUTTON_WIDTH = 50f

    private val buttonOk: TextButton = TextButton(loc.get("ok"), skin)
    private val titleLabel: Label = Label(loc.get("your.position.is"), skin)
    private val rankLabel: Label = Label("${rank}.", skin, "menuTitle")

    private var screen: PlayerSettingsScreen? = null

    init {
        titleLabel.setFontScale(0.40f)
        contentTable.add(titleLabel)
                .center()
                .padTop(10f)
                .row()

        rankLabel.setFontScale(0.5f)
        contentTable.add(rankLabel)
                .center()
                .row()

        buttonOk.style.font.setScale(0.5f)
        buttonOk.addListener(clicked {
            hide()
            switchScreen(screen)
        })

        contentTable.add(buttonOk)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .bottom()
                .padBottom(10f)
                .row()
    }
}
