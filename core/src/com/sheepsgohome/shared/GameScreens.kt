package com.sheepsgohome.shared

import com.sheepsgohome.screens.GameplayClassicModeScreen
import com.sheepsgohome.screens.MainMenuScreen
import com.sheepsgohome.screens.SplashScreen

object GameScreens {
    val splashScreen by lazy { SplashScreen() }
    val mainMenuScreen by lazy { MainMenuScreen() }
    val gameplayClassicModeScreen by lazy { GameplayClassicModeScreen() }
}
