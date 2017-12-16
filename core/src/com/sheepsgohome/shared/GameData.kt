package com.sheepsgohome.shared

import com.badlogic.gdx.Application.ApplicationType.Desktop
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.sheepsgohome.android.Android
import com.sheepsgohome.google.GoogleLeaderboard
import com.sheepsgohome.google.GoogleMultiplayer


object GameData {
    private val gamePreferences: Preferences by lazy { Gdx.app.getPreferences("gamePreferences") }

    //----------------ANDROID BRIDGE-----------------
    var android: Android? = null
    var leaderboard: GoogleLeaderboard? = null
    var multiplayer: GoogleMultiplayer? = null

    //----------------CONSTANTS----------------------
    val VERSION_STRING = "1.0.4"

    val CAMERA_HEIGHT = 160f
    val CAMERA_WIDTH = 90f

    val SETTINGS_ITEM_FONT_SCALE = 0.5f

    //----------------PREFERENCES----------------------
    var VIRTUAL_JOYSTICK = 0
    var VIRTUAL_JOYSTICK_NONE = 0
    var VIRTUAL_JOYSTICK_LEFT = 1
    var VIRTUAL_JOYSTICK_RIGHT = 2

    var SOUND_ENABLED = true
    var SOUND_VOLUME = 0.7f
    var MUSIC_ENABLED = true

    //----------------GAME-DATA----------------------
    var LEVEL = 1
    var LEVEL_LASERS = 1

    fun loadPreferences() {
        with(gamePreferences) {
            LEVEL = getInteger("LEVEL", 1)
            LEVEL_LASERS = getInteger("LEVEL_LASERS", 1)

            MUSIC_ENABLED = getBoolean("MUSICENABLED", true)
            SOUND_ENABLED = getBoolean("SOUNDENABLED", true)
            SOUND_VOLUME = getFloat("SOUNDVOLUME", 0.5f)

            val preferredJoystick = when (Gdx.app.type) {
                Desktop -> VIRTUAL_JOYSTICK_NONE
                else -> VIRTUAL_JOYSTICK_RIGHT
            }

            VIRTUAL_JOYSTICK = getInteger("VIRTUALJOYSTICK", preferredJoystick)
        }
    }

    fun savePreferences() {
        with(gamePreferences) {
            putInteger("LEVEL", LEVEL)
            putInteger("LEVEL_LASERS", LEVEL_LASERS)
            putInteger("VIRTUALJOYSTICK", VIRTUAL_JOYSTICK)
            putBoolean("SOUNDENABLED", SOUND_ENABLED)
            putBoolean("MUSICENABLED", MUSIC_ENABLED)
            putFloat("SOUNDVOLUME", SOUND_VOLUME)
            flush()
        }
    }

    fun levelUp() {
        LEVEL++
        gamePreferences.putInteger("LEVEL", GameData.LEVEL)
        gamePreferences.flush()
    }
}
