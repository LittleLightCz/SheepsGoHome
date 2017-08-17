package com.sheepsgohome

import com.badlogic.gdx.Application.ApplicationType.Desktop
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.I18NBundle
import com.sheepsgohome.interfaces.FunctionsInterface
import com.sheepsgohome.leaderboard.LeaderBoard


object GameData {
    //----------------CONSTANTS----------------------
    val VERSION_STRING = "1.0.2"

    var codeX = "oiaewj023897rvoiuwanvoiune0v9128n09r2898v7q3342089"

    val CAMERA_HEIGHT = 160f
    val CAMERA_WIDTH = 90f

    val SETTINGS_TITLE_FONT_SCALE = 0.35f
    val SETTINGS_ITEM_FONT_SCALE = 0.5f

    val SHEEP_SIZE = 6f
    val HOME_SIZE = 12f

    val WILD_WOLF_SIZE = 6f
    val HUNGRY_WOLF_SIZE = 8f
    val ALPHA_WOLF_SIZE = 10f

    var SHEEP_SPEED = 20f
    var WILD_WOLF_SPEED = 8f
    var HUNGRY_WOLF_SPEED = 10f
    var ALPHA_WOLF_SPEED = 1.2f

    var VIRTUAL_JOYSTICK_NONE = 0
    var VIRTUAL_JOYSTICK_LEFT = 1
    var VIRTUAL_JOYSTICK_RIGHT = 2

    //----------------GAME-DATA----------------------
    var LEVEL: Int = 1
    var PLAYER_NAME = ""

    //----------------PREFERENCES----------------------
    var VIRTUAL_JOYSTICK = 0
    var SOUND_ENABLED = true
    var SOUND_VOLUME = 0.7f
    var MUSIC_ENABLED = true

    lateinit var gamePreferences: Preferences
    lateinit var functions: FunctionsInterface
    lateinit var Loc: I18NBundle
    lateinit var leaderboard: LeaderBoard

    fun LoadPreferences() {
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

    fun SavePreferences() {
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
}
