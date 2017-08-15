package com.sheepsgohome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.I18NBundle;
import com.sheepsgohome.interfaces.FunctionsInterface;
import com.sheepsgohome.leaderboard.LeaderBoard;


public class GameData {
    //----------------CONSTANTS----------------------
    public static final String VERSION_STRING = "1.0.2";

    public static String codeX = "oiaewj023897rvoiuwanvoiune0v9128n09r2898v7q3342089";

    public static final float CAMERA_HEIGHT = 160;
    public static final float CAMERA_WIDTH = 90;

    public static final float SETTINGS_TITLE_FONT_SCALE = 0.35f;
    public static final float SETTINGS_ITEM_FONT_SCALE = 0.5f;

    public static final float SHEEP_SIZE = 6;
    public static final float HOME_SIZE = 12;

    public static final float WILD_WOLF_SIZE = 6;
    public static final float HUNGRY_WOLF_SIZE = 8;
    public static final float ALPHA_WOLF_SIZE = 10;

    public static float SHEEP_SPEED = 20;
    public static float WILD_WOLF_SPEED = 8;
    public static float HUNGRY_WOLF_SPEED = 10;
    public static float ALPHA_WOLF_SPEED = 1.2f;

    public static int VIRTUAL_JOYSTICK_NONE = 0;
    public static int VIRTUAL_JOYSTICK_LEFT = 1;
    public static int VIRTUAL_JOYSTICK_RIGHT = 2;

    //----------------GAME-DATA----------------------
    public static int LEVEL;
    public static String PLAYER_NAME;

    //----------------PREFERENCES----------------------
    public static int VIRTUAL_JOYSTICK;
    public static boolean SOUND_ENABLED = true;
    public static float SOUND_VOLUME = 0.7f;
    public static boolean MUSIC_ENABLED = true;

    public static Preferences gamePreferences;
    public static FunctionsInterface functions;
    public static I18NBundle Loc;
    public static LeaderBoard leaderboard;

    public static void LoadPreferences() {
        LEVEL = gamePreferences.getInteger("LEVEL", 1);

        MUSIC_ENABLED = gamePreferences.getBoolean("MUSICENABLED", true);
        SOUND_ENABLED = gamePreferences.getBoolean("SOUNDENABLED", true);

        SOUND_VOLUME = gamePreferences.getFloat("SOUNDVOLUME", 0.5f);

        PLAYER_NAME = gamePreferences.getString("PLAYERNAME", "");

        int preferredJoystick;
        switch (Gdx.app.getType()) {
            case Desktop:
                preferredJoystick = VIRTUAL_JOYSTICK_NONE;
                break;
            default:
                preferredJoystick = VIRTUAL_JOYSTICK_RIGHT;
                break;
        }

        VIRTUAL_JOYSTICK = gamePreferences.getInteger("VIRTUALJOYSTICK", preferredJoystick);
    }

    public static void SavePreferences() {
        gamePreferences.putInteger("LEVEL", LEVEL);
        gamePreferences.putInteger("VIRTUALJOYSTICK", VIRTUAL_JOYSTICK);

        gamePreferences.putBoolean("SOUNDENABLED", SOUND_ENABLED);

        gamePreferences.putBoolean("MUSICENABLED", MUSIC_ENABLED);

        gamePreferences.putFloat("SOUNDVOLUME", SOUND_VOLUME);

        gamePreferences.putString("PLAYERNAME", PLAYER_NAME);

        gamePreferences.flush();
    }
}
