package com.sheepsgohome.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sheepsgohome.shared.GameSkins

class ScreenTitle(title: String) : Label(title, GameSkins.skin, "menuTitle") {

    private val SETTINGS_TITLE_FONT_SCALE = 0.35f

    init {
        setFontScale(SETTINGS_TITLE_FONT_SCALE)
    }
}