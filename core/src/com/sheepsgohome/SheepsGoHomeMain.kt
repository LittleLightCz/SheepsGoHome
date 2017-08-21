package com.sheepsgohome

import com.badlogic.gdx.Game
import com.sheepsgohome.screens.SplashScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameMusic
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSounds

class SheepsGoHomeMain : Game() {

    override fun create() {
        GameData.loadPreferences()

        GameMusic.load()
        GameSounds.load()

        setScreen(SplashScreen())
    }

}
