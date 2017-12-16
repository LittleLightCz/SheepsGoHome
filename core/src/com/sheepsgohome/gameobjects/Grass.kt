package com.sheepsgohome.gameobjects


import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

class Grass {

    private val grassTexture = Texture("grass-background.jpg").apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private val sprite = Sprite(grassTexture).apply {
        setPosition(-CAMERA_WIDTH, -CAMERA_HEIGHT)
        setSize(CAMERA_WIDTH * 2, CAMERA_HEIGHT * 2)
    }

    val translateY = sprite::translateY

    fun draw(batch: SpriteBatch) = sprite.draw(batch)

    fun dispose() {
        grassTexture.dispose()
    }
}