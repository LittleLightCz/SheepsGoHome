package com.sheepsgohome.gdx.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.sheepsgohome.screens.GameplayClassicModeScreen
import com.sheepsgohome.screens.MainMenuScreen

fun switchScreen(screen: Screen?) {
    if (screen != null) {
        (Gdx.app.applicationListener as Game).screen = screen
    }
}

fun switchToGameplayClassicModeScreen() = switchScreen(GameplayClassicModeScreen())
fun switchToMainMenuScreen() = switchScreen(MainMenuScreen())
