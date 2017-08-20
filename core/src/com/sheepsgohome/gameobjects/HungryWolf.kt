package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.SteerableHungryWolfBody

private val HUNGRY_WOLF_SIZE = 8f

class HungryWolf(world: World, sheep: Sheep) : Wolf(
        world,
        HUNGRY_WOLF_SIZE,
        FixtureDef().apply {
            density = 0.2f
            friction = 0.1f
            restitution = 0.6f
        }
) {

    private val steerableBody = SteerableHungryWolfBody(body, sheep)

    val texture = Texture("wolf-hungry.png")

    override val sprite = Sprite(texture).apply {
        setSize(HUNGRY_WOLF_SIZE, HUNGRY_WOLF_SIZE)
        setOriginCenter()
    }

    fun calculateSteeringBehaviour() {
        steerableBody.calculateSteeringBehaviour()
    }

    override fun updateSprite() = updateSprite(steerableBody.body, HUNGRY_WOLF_SIZE)

    override fun transformBody(x: Float, y: Float, angle: Float) = transformBody(steerableBody.body, x, y, angle)

    override fun dispose() {
        texture.dispose()
    }


}