package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.GameTools
import java.util.*

class WildWolf(world: World) : Wolf(world) {

    companion object {
        val WILD_WOLF_SIZE = 6f
        var WILD_WOLF_SPEED = 8f
    }

    override val fixtureDef = FixtureDef().apply {
        shape = CircleShape().apply {
            radius = WILD_WOLF_SIZE / 2 * 0.85f
        }
        density = 0.1f
        friction = 0.1f
        restitution = 0.6f
    }

    private val body = createWolfBody()

    private val texture = Texture("wolf.png")

    override val sprite = Sprite(texture).apply {
        setSize(WILD_WOLF_SIZE, WILD_WOLF_SIZE)
        setOriginCenter()
    }

    //Refactor!

    val random = Random()

    private val randomFloat: Float
        get() = GameTools.random.nextFloat() * 2f - 1f

    fun setRandomMovement() {
        body.setLinearVelocity(randomFloat * WILD_WOLF_SPEED, randomFloat * WILD_WOLF_SPEED)
    }

    override fun updateSprite() = updateSprite(body, WILD_WOLF_SIZE)

    override fun transformBody(x: Float, y: Float, angle: Float) = transformBody(body, x, y, angle)

    override fun dispose() {
        texture.dispose()
    }

}

