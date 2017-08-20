package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import java.util.*

class WildWolf(world: World) : Wolf(
        world,
        FixtureDef().apply {
            density = 0.1f
            friction = 0.1f
            restitution = 0.6f
        },
        WILD_WOLF_SIZE,
        Texture("wolf.png")
) {

    companion object {
        val WILD_WOLF_SIZE = 6f
        var WILD_WOLF_SPEED = 8f
    }

    private val random = Random()

    private val randomFloat
        get() = random.nextFloat() * 2f - 1f

    init {
        body.userData = this
    }

    fun setRandomMovement() {
        body.setLinearVelocity(randomFloat * WILD_WOLF_SPEED, randomFloat * WILD_WOLF_SPEED)
    }
}

