package com.sheepsgohome.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.gdx.screens.switchToGameplayClassicModeScreen
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameMusic
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class ChooseModeScreen : MenuScreen() {

    private val buttonClassicMode = BigSheepButton(Loc.classic)
    private val buttonLasersMode = BigSheepButton(Loc.lasers)

    private val buttonBack = SmallSheepButton(Loc.back)

    private val title = ScreenTitle(Loc.chooseMode)

    init {

        buttonClassicMode.onClick {
            runGameScreen {
                switchToGameplayClassicModeScreen()
            }
        }

        buttonLasersMode.onClick {
            runGameScreen {

            }
        }

        buttonBack.onClick {
            switchToMainMenuScreen()
        }

        table.add(title)
                .top()
                .row()

        val buttonsTable = Table()

        buttonClassicMode.addTo(buttonsTable).row()
        buttonLasersMode.addTo(buttonsTable).row

        table.add(buttonsTable).expandY().row()

        buttonBack.addTo(table)
                .bottom()
                .row()

    }

    private fun runGameScreen(action: () -> Unit) {
        if (GameMusic.sheepsTheme.isPlaying) {
            GameMusic.sheepsTheme.pause()
        }

        action()
    }

}
