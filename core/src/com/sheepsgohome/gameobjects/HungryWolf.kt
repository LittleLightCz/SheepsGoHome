package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.SteerableHungryWolfBody

class HungryWolf(world: World, sheep: Sheep) : Wolf(world) {

    val HUNGRY_WOLF_SIZE = 8f

    override val fixtureDef = FixtureDef().apply {
        shape = CircleShape().apply {
            radius = HUNGRY_WOLF_SIZE / 2 * 0.85f
        }
        density = 0.2f
        friction = 0.1f
        restitution = 0.6f
    }

    private val steerableBody = SteerableHungryWolfBody(createWolfBody(), sheep)

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