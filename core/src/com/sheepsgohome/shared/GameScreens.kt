package com.sheepsgohome.shared

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.sheepsgohome.screens.GameplayClassicModeScreen
import com.sheepsgohome.screens.MainMenuScreen
import com.sheepsgohome.screens.SplashScreen

object GameScreens {
    val splashScreen by lazy { SplashScreen() }
    val mainMenuScreen by lazy { MainMenuScreen() }
    val gameplayClassicModeScreen by lazy { GameplayClassicModeScreen() }

    fun switchScreen(screen: Screen?) {
        if (screen != null) {
            (Gdx.app.applicationListener as Game).screen = screen
        }
    }
}
