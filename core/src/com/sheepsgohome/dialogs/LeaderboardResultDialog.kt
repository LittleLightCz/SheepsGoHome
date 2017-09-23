package com.sheepsgohome.dialogs

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SmallSheepButton

class LeaderboardResultDialog(rank: Long) : AbstractFixedSizeDialog() {

    private val buttonOk = SmallSheepButton(loc.get("ok"))
    private val titleLabel = Label(loc.get("your.position.is"), skin)
    private val rankLabel = Label("$rank.", skin, "menuTitle")

    private var screen: Screen? = null

    init {
        fixedHeight = 70f

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

        buttonOk.addTo(contentTable)
                .bottom()
                .padBottom(10f)
                .row()

    }
}
