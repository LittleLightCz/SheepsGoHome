package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.SteerableHungryWolfBody

class HungryWolf(world: World, sheep: Sheep) : Wolf(
        world,
        FixtureDef().apply {
            density = 0.2f
            friction = 0.1f
            restitution = 0.6f
        },
        HUNGRY_WOLF_SIZE,
        Texture("wolf-hungry.png")
) {

    companion object {
        val HUNGRY_WOLF_SIZE = 8f
        val HUNGRY_WOLF_SPEED = 10f
    }

    private val steerableBody = SteerableHungryWolfBody(body, sheep)

    init {
        body.userData = this
    }

    fun calculateSteeringBehaviour() {
        steerableBody.calculateSteeringBehaviour()
    }
}