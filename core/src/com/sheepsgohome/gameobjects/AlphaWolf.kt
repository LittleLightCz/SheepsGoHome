package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData

class AlphaWolf(world: World, val sheep: Sheep) : Wolf(
        world,
        FixtureDef().apply {
            density = 0.3f
            friction = 0.1f
            restitution = 0.6f
        },
        ALPHA_WOLF_SIZE,
        Texture("wolf-alpha.png")
) {

    companion object {
        private val ALPHA_WOLF_SIZE = 10f
        private val ALPHA_WOLF_SPEED = 1.2f
    }

    init {
        body.userData = this
    }

    fun updateVelocity() {
        val sheepPosition = sheep.steerableBody.position

        val vec = Vector2(sheepPosition).sub(body.position).nor()
        val alphaSpeed = ALPHA_WOLF_SPEED / Math.abs(body.position.dst(sheepPosition) / GameData.CAMERA_HEIGHT)

        body.setLinearVelocity(vec.x * alphaSpeed, vec.y * alphaSpeed)
    }

}