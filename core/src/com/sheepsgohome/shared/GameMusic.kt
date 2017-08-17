package com.sheepsgohome.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

object GameMusic {
    lateinit var sheepsTheme: Music
    lateinit var ambient: Music

    fun load() {
        //music
        sheepsTheme = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_theme.mp3")).apply {
            isLooping = true
            volume = GameData.SOUND_VOLUME
        }

        //ambient
        ambient = Gdx.audio.newMusic(Gdx.files.internal("music/sheeps_ambient.mp3")).apply {
            isLooping = true
            volume = GameData.SOUND_VOLUME
        }
    }
}
