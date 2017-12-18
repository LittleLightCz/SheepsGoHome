package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameTools
import com.sheepsgohome.steerable.SteerableBody

open class SteerableGameObject(world: World) {

    private val bodyDef = BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
        position.set(0f, 0f)
    }

    val steerableBody = SteerableBody(world.createBody(bodyDef))

    open fun updateSprite(sprite: Sprite, spriteSize: Float) {
        with(steerableBody.position) {
            sprite.setPosition(x - spriteSize / 2, y - spriteSize / 2)
        }

        sprite.rotation = Math.toDegrees(steerableBody.body.angle.toDouble()).toFloat()
    }

    fun updateBodyVelocity(directionX: Float, directionY: Float, speedMultiplier: Float) {
        steerableBody.body.setLinearVelocity(directionX * speedMultiplier, directionY * speedMultiplier)
        updateBodyAngle(GameTools.vectorAngleRadians(directionX, directionY))
    }

    fun updateBodyVelocity(direction: Vector2, speedMultiplier: Float) = updateBodyVelocity(direction.x, direction.y, speedMultiplier)

    fun updateBodyAngle(velocity: Vector2) = updateBodyAngle(velocity.x, velocity.y)

    fun updateBodyAngle(directionX: Float, directionY: Float) = updateBodyAngle(GameTools.vectorAngleRadians(directionX, directionY))

    fun updateBodyAngle(angleRadians: Float) {
        with(steerableBody) {
            body.setTransform(position, angleRadians)
        }
    }

    fun nullifyVelocity() {
        with(steerableBody.body) {
            setLinearVelocity(0f, 0f)
            angularVelocity = 0f
        }
    }
}