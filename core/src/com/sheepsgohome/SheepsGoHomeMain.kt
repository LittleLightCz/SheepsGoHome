package com.sheepsgohome

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.sheepsgohome.GameSkins.skin
import com.sheepsgohome.dialogs.ContinueLastGameDialog
import com.sheepsgohome.interfaces.FunctionsInterface
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.SOUND_VOLUME

class SheepsGoHomeMain(functions: FunctionsInterface) : Game() {

    init {
        GameData.functions = functions
    }

    override fun create() {

        GameData.loadPreferences()

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

}
