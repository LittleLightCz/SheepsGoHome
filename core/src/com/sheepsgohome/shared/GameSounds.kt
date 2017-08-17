package com.sheepsgohome.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

object GameSounds {
    lateinit var soundWolfFailure: Sound
    lateinit var soundSheepSuccess: Sound
    lateinit var soundNewBadge: Sound

    fun load() {
        soundWolfFailure = Gdx.audio.newSound(Gdx.files.internal("sound/wolf_failure.mp3"))
        soundSheepSuccess = Gdx.audio.newSound(Gdx.files.internal("sound/sheep_success.mp3"))
        soundNewBadge = Gdx.audio.newSound(Gdx.files.internal("sound/new_badge.mp3"))
    }
}
