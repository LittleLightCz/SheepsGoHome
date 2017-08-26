package com.sheepsgohome.shared

import com.badlogic.gdx.Application.ApplicationType.Desktop
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.I18NBundle
import com.sheepsgohome.android.Android
import com.sheepsgohome.google.GoogleLeaderboard
import java.util.*


object GameData {
    private val gamePreferences: Preferences by lazy { Gdx.app.getPreferences("gamePreferences") }

    val loc: I18NBundle by lazy {
        val baseFileHandle = Gdx.files.internal("loc/Language")
        val locale = Locale.getDefault()
        I18NBundle.createBundle(baseFileHandle, locale)
    }

    //----------------ANDROID BRIDGE-----------------
    var android: Android? = null
    var leaderboard: GoogleLeaderboard? = null

    //----------------CONSTANTS----------------------
    val VERSION_STRING = "1.0.3"
    val codeX = "oiaewj023897rvoiuwanvoiune0v9128n09r2898v7q3342089"

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
    var PLAYER_NAME = ""

    fun loadPreferences() {
        with(gamePreferences) {
            LEVEL = getInteger("LEVEL", 1)

            MUSIC_ENABLED = getBoolean("MUSICENABLED", true)
            SOUND_ENABLED = getBoolean("SOUNDENABLED", true)
            SOUND_VOLUME = getFloat("SOUNDVOLUME", 0.5f)

            PLAYER_NAME = getString("PLAYERNAME", "")

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
            putInteger("VIRTUALJOYSTICK", VIRTUAL_JOYSTICK)
            putBoolean("SOUNDENABLED", SOUND_ENABLED)
            putBoolean("MUSICENABLED", MUSIC_ENABLED)
            putFloat("SOUNDVOLUME", SOUND_VOLUME)
            putString("PLAYERNAME", PLAYER_NAME)
            flush()
        }
    }

    fun levelUp() {
        LEVEL++
        gamePreferences.putInteger("LEVEL", GameData.LEVEL)
        gamePreferences.flush()
    }
}
