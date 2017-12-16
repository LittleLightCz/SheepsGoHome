package com.tumblr.svetylk0.sheepsgohome.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.sheepsgohome.SheepsGoHomeMain

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = 1080 / 3
        config.height = 1920 / 3

        LwjglApplication(SheepsGoHomeMain(), config)
    }
}
