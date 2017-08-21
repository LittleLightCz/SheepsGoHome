package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SmallSheepButton

class SettingsControlsScreen : MenuScreen() {

    private val buttonSave = SmallSheepButton(loc.get("save"))
    private val buttonBack = SmallSheepButton(loc.get("back"))

    private val title = Label(loc.get("controls"), skin, "menuTitle")
    private val virtualJoystickTitle = Label(loc.get("virtual.joystick"), skin, "default")

    init {
        val virtualJoystickSelectBox = SelectBox<String>(skin)
        virtualJoystickSelectBox.setItems(loc.get("none"), loc.get("left"), loc.get("right"))
        virtualJoystickSelectBox.selectedIndex = VIRTUAL_JOYSTICK

        //click listeners
        buttonSave.onClick {
            VIRTUAL_JOYSTICK = virtualJoystickSelectBox.selectedIndex
            GameData.savePreferences()
            switchScreen(SettingsScreen())
        }

        buttonBack.onClick {
            switchScreen(SettingsScreen())
        }

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add(title)
                .top()
                .colspan(2)
                .row()

        val contentTable = Table()

        virtualJoystickTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE)
        contentTable.add(virtualJoystickTitle)
                .expandX()
                .padRight(2f)

        contentTable.add(virtualJoystickSelectBox)
                .expandX()
                .width(50f)
                .height(20f)
                .row()

        table.add(contentTable)
                .expand()
                .colspan(2)
                .top()
                .row()

        buttonSave.addTo(table)
                .bottom()
                .right()

        buttonBack.addTo(table)
                .bottom()
                .left()
                .row()

        Gdx.input.inputProcessor = stage
    }

}
