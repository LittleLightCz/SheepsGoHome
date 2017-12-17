package com.sheepsgohome.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.onClick
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton

class SoundSettingsScreen : MenuScreen() {

    private val saveButton = SmallSheepButton(Loc.save)
    private val backButton = SmallSheepButton(Loc.back)

    private val title = ScreenTitle(Loc.sound)
    private val soundEnabledTitle = Label(Loc.soundEnabled, skin)

    init {
        val soundEnabledSelectBox = SelectBox<String>(skin)
        soundEnabledSelectBox.setItems(Loc.yes, Loc.no)
        soundEnabledSelectBox.selectedIndex = if (SOUND_ENABLED) 0 else 1

        //click listeners
        saveButton.onClick {
            SOUND_ENABLED = soundEnabledSelectBox.selectedIndex == 0
            GameData.savePreferences()
            switchScreen(SettingsScreen())
        }

        backButton.onClick {
            switchScreen(SettingsScreen())
        }

        table.add(title)
                .top()
                .colspan(2)
                .row()

        val contentTable = Table()

        soundEnabledTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE)
        contentTable.add(soundEnabledTitle)
                .expandX()
                .padRight(2f)

        contentTable.add(soundEnabledSelectBox)
                .expandX()
                .width(50f)
                .height(20f)
                .row()


        table.add(contentTable)
                .expand()
                .colspan(2)
                .top()
                .row()

        saveButton.addTo(table)
                .bottom()
                .right()

        backButton.addTo(table)
                .bottom()
                .left()
                .row()

    }

}
