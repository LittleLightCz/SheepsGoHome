package com.sheepsgohome.gameobjects

import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData.HUNGRY_WOLF_SIZE

class HungryWolf(world: World, sheep: Sheep) : Wolf(world) {

    override val fixtureDef = FixtureDef().apply {
        shape = CircleShape().apply {
            radius = HUNGRY_WOLF_SIZE / 2 * 0.85f
        }
        density = 0.2f
        friction = 0.1f
        restitution = 0.6f
    }

    private val body = initializeWolfBody()
}