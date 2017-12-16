package com.sheepsgohome.gdx.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.sheepsgohome.screens.MainMenuScreen
import com.sheepsgohome.screens.gameplay.GameplayClassicModeScreen

fun switchScreen(screen: Screen?) {
    if (screen != null) {
        (Gdx.app.applicationListener as Game).screen = screen
    }
}

fun switchToGameplayClassicModeScreen() = switchScreen(GameplayClassicModeScreen())
fun switchToMainMenuScreen() = switchScreen(MainMenuScreen())
