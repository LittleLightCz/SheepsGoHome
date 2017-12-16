package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class SettingsScreen : MenuScreen() {

    private val buttonControls = BigSheepButton(Loc.controls)
    private val buttonSound = BigSheepButton(Loc.sound)

    private val buttonBack = SmallSheepButton(Loc.back)

    private val title = ScreenTitle(Loc.settings)

    init {

        buttonControls.onClick {
            switchScreen(ControlsSettingsScreen())
        }

        buttonSound.onClick {
            switchScreen(SoundSettingsScreen())
        }

        buttonBack.onClick {
            switchToMainMenuScreen()
        }

        table.add(title)
                .top()
                .row()

        val buttonsTable = Table()

        buttonControls.addTo(buttonsTable).row()
        buttonSound.addTo(buttonsTable).row()

        table.add(buttonsTable).expandY().row()

        buttonBack.addTo(table)
                .bottom()
                .row()

    }

}
