package com.sheepsgohome.badges

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.sheepsgohome.shared.GameData.loc

class Badge(val badgeNumber: Int) {
    val texture = Texture("badges/badge$badgeNumber.png").apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    val image = Image(texture)

    val badgeName: String by lazy {
        when (badgeNumber) {
            1 -> loc.get("pasture")
            2 -> loc.get("threat.awareness")
            3 -> loc.get("alpha.defeater")
            4 -> loc.get("escapist")
            5 -> loc.get("agility")
            6 -> loc.get("tactician")
            7 -> loc.get("fearless")
            8 -> loc.get("sheeps.defiance")
            9 -> loc.get("limitless.courage")
            10 -> loc.get("ruthless.conspiracy")
            11 -> loc.get("wolf.apocalypse")
            12 -> loc.get("sheep.master")
            else -> "UNKNOWN"
        }
    }

    fun dispose() {
        texture.dispose()
    }

    fun clone() = Badge(badgeNumber)

}