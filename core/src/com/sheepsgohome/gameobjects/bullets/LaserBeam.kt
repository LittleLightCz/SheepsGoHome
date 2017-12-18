package com.sheepsgohome.gameobjects.bullets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.sheepsgohome.gameobjects.SteerableGameObject

class LaserBeam(val world: World): SteerableGameObject(world), Disposable {

    private val LASER_BEAM_SIZE = 3f
    private val LASER_BEAM_SPEED = 40f
    private val MAX_ALLOWED_COLLISIONS = 3

    private var collisionsCount = 0

    var shouldBeDisposed = false
        private set

    private val texture = Texture("sheepLaserBeam.png")

    private val sprite = Sprite(texture).apply {
        setSize(LASER_BEAM_SIZE, LASER_BEAM_SIZE)
        setOriginCenter()
    }

    init {
        val circleShape = CircleShape().apply {
            radius = (LASER_BEAM_SIZE - 1) / 2
        }

        val fixtureDef = FixtureDef().apply {
            shape = circleShape
            density = 0f
            friction = 0f
            restitution = 1f
        }

        steerableBody.body.createFixture(fixtureDef)
        steerableBody.body.userData = this

        circleShape.dispose()
    }

    fun handleCollision() {
        collisionsCount++

        if (collisionsCount > MAX_ALLOWED_COLLISIONS) {
            shouldBeDisposed = true
        }
    }

    fun updateSprite() {
        updateSprite(sprite, LASER_BEAM_SIZE)
    }

    fun updateAngle() {
        updateBodyAngle(steerableBody.body.linearVelocity)
    }

    fun draw(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    override fun dispose() {
        texture.dispose()
        world.destroyBody(steerableBody.body)
    }

    fun setPosition(x: Float, y: Float) {
        steerableBody.body.setTransform(x, y, 0f)
    }

    fun shootInDirection(vector: Vector2) {
        steerableBody.body.setLinearVelocity(vector.x * LASER_BEAM_SPEED, vector.y * LASER_BEAM_SPEED)
    }

}