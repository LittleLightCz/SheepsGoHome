package com.sheepsgohome.badges

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.sheepsgohome.localization.Loc

class Badge(val badgeNumber: Int) {
    val texture = Texture("badges/badge$badgeNumber.png").apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    val image = Image(texture)

    val badgeName: String by lazy {
        when (badgeNumber) {
            1 -> Loc.pasture
            2 -> Loc.threatAwareness
            3 -> Loc.alphaDefeater
            4 -> Loc.escapist
            5 -> Loc.agility
            6 -> Loc.tactician
            7 -> Loc.fearless
            8 -> Loc.sheepsDefiance
            9 -> Loc.limitlessCourage
            10 -> Loc.ruthlessConspiracy
            11 -> Loc.wolfApocalypse
            12 -> Loc.sheepMaster
            else -> "UNKNOWN"
        }
    }

    fun dispose() {
        texture.dispose()
    }

    fun clone() = Badge(badgeNumber)

}