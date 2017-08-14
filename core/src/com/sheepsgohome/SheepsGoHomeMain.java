package com.sheepsgohome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.sheepsgohome.dialogs.ContinueLastGameDialog;
import com.sheepsgohome.interfaces.FunctionsInterface;
import com.sheepsgohome.leaderboard.LeaderBoard;
import com.sheepsgohome.screens.GameplayClassicModeScreen;
import com.sheepsgohome.screens.MainMenuScreen;
import com.sheepsgohome.screens.SplashScreen;

import java.util.Locale;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import static com.sheepsgohome.GameData.SOUND_VOLUME;
import static com.sheepsgohome.GameSkins.skin;

public class SheepsGoHomeMain extends Game {

    public SheepsGoHomeMain(FunctionsInterface d) {
        GameData.functions = d;
    }

    @Override
    public void create() {

        //Load preferences + game data
        GameData.gamePreferences = Gdx.app.getPreferences("gamePreferences");
//        GameData.gameplayGlobalPreferences =  Gdx.app.getPreferences("gameplayGlobalPreferences");
        GameData.LoadPreferences();
        GameData.leaderboard = new LeaderBoard();

        loadSkin();

        loadLanguage();

        GameScreens.splashScreen = new SplashScreen();
        GameScreens.mainMenuScreen = new MainMenuScreen();
        GameScreens.gameplayClassicModeScreen = new GameplayClassicModeScreen();

        GameDialogs.dialogContinueLastGame = new ContinueLastGameDialog("", skin, "default");

        //music
        GameMusic.music = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_theme.mp3"));
        GameMusic.music.setLooping(true);
        GameMusic.music.setVolume(SOUND_VOLUME);

        //ambient
        GameMusic.ambient = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_ambient.mp3"));
        GameMusic.ambient.setLooping(true);
        GameMusic.ambient.setVolume(SOUND_VOLUME);

        //sounds
        GameSounds.soundWolfFailure = Gdx.audio.newSound(Gdx.files.internal("sound/wolf_failure.mp3"));
        GameSounds.soundSheepSuccess = Gdx.audio.newSound(Gdx.files.internal("sound/sheep_success.mp3"));
        GameSounds.soundNewBadge = Gdx.audio.newSound(Gdx.files.internal("sound/new_badge.mp3"));

        setScreen(GameScreens.splashScreen);
    }

    private void loadLanguage() {
        FileHandle baseFileHandle = Gdx.files.internal("loc/Language");
        Locale locale = Locale.getDefault();
        GameData.Loc = I18NBundle.createBundle(baseFileHandle, locale);
    }

    private void loadSkin() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/osifont.ttf"));
        FreeTypeFontGenerator generatorDecor = new FreeTypeFontGenerator(Gdx.files.internal("fonts/akaDylanPlain.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        skin = new Skin();

        //default
        parameter.characters += "’řšžťčůňěďŽ";
        parameter.size = 30;
        skin.add("font", generator.generateFont(parameter));

        //menu
        parameter.size = 40;
        skin.add("font_menuTitle", generatorDecor.generateFont(parameter));

        skin.getFont("font_menuTitle").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        skin.getFont("font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skins/main.pack")));
        skin.load(Gdx.files.internal("skins/skinMain.json"));

        generator.dispose();
        generatorDecor.dispose();

        //touchpad
        skin.getRegion("touchpadbg").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        skin.getRegion("selectbox").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //music on/off checkbox
        skin.getRegion("music_on").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        skin.getRegion("music_off").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //cursor
//        skin.getRegion("text_cursor").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

    }

}
