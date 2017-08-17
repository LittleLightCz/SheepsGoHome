package com.sheepsgohome

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sheepsgohome.GameSkins.skin
import com.sheepsgohome.dialogs.ContinueLastGameDialog
import com.sheepsgohome.interfaces.FunctionsInterface
import com.sheepsgohome.screens.GameplayClassicModeScreen
import com.sheepsgohome.screens.MainMenuScreen
import com.sheepsgohome.screens.SplashScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.SOUND_VOLUME

class SheepsGoHomeMain(functions: FunctionsInterface) : Game() {

    init {
        GameData.functions = functions
    }

    override fun create() {
        //Load preferences + game data
        GameData.LoadPreferences()

        loadSkin()

        GameScreens.splashScreen = SplashScreen()
        GameScreens.mainMenuScreen = MainMenuScreen()
        GameScreens.gameplayClassicModeScreen = GameplayClassicModeScreen()

        GameDialogs.dialogContinueLastGame = ContinueLastGameDialog("", skin, "default")

        //music
        GameMusic.music = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_theme.mp3"))
        GameMusic.music.isLooping = true
        GameMusic.music.volume = SOUND_VOLUME

        //ambient
        GameMusic.ambient = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_ambient.mp3"))
        GameMusic.ambient.isLooping = true
        GameMusic.ambient.volume = SOUND_VOLUME

        //sounds
        GameSounds.soundWolfFailure = Gdx.audio.newSound(Gdx.files.internal("sound/wolf_failure.mp3"))
        GameSounds.soundSheepSuccess = Gdx.audio.newSound(Gdx.files.internal("sound/sheep_success.mp3"))
        GameSounds.soundNewBadge = Gdx.audio.newSound(Gdx.files.internal("sound/new_badge.mp3"))

        setScreen(GameScreens.splashScreen)
    }

    private fun loadSkin() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/osifont.ttf"))
        val generatorDecor = FreeTypeFontGenerator(Gdx.files.internal("fonts/akaDylanPlain.ttf"))
        val parameter = FreeTypeFontParameter()

        skin = Skin()

        //default
        parameter.characters += "’řšžťčůňěďŽ"
        parameter.size = 30
        skin.add("font", generator.generateFont(parameter))

        //menu
        parameter.size = 40
        skin.add("font_menuTitle", generatorDecor.generateFont(parameter))

        skin.getFont("font_menuTitle").region.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        skin.getFont("font").region.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        skin.addRegions(TextureAtlas(Gdx.files.internal("skins/main.pack")))
        skin.load(Gdx.files.internal("skins/skinMain.json"))

        generator.dispose()
        generatorDecor.dispose()

        //touchpad
        skin.getRegion("touchpadbg").texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        skin.getRegion("selectbox").texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        //music on/off checkbox
        skin.getRegion("music_on").texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        skin.getRegion("music_off").texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        //cursor
        //        skin.getRegion("text_cursor").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

    }

}
