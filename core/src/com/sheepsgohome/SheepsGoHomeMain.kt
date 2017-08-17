package com.sheepsgohome

import com.badlogic.gdx.Game
import com.sheepsgohome.dialogs.ContinueLastGameDialog
import com.sheepsgohome.interfaces.FunctionsInterface
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameMusic
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.shared.GameSounds

class SheepsGoHomeMain(functions: FunctionsInterface) : Game() {

    init {
        GameData.functions = functions
    }

    override fun create() {
        GameData.loadPreferences()

        GameDialogs.dialogContinueLastGame = ContinueLastGameDialog("", skin, "default")

        GameMusic.load()
        GameSounds.load()

        setScreen(GameScreens.splashScreen)
    }

}
