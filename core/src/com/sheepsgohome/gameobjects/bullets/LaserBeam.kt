package com.sheepsgohome.gameobjects.bullets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.sheepsgohome.steerable.SteerableBody

class LaserBeam(val world: World): Disposable {

    private val SIZE = 2f
    private val SPEED = 40f

    var collisionCount = 0
    var disposed = false

    private val bodyDef = BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
        position.set(0f, 0f)
    }

    val steerableBody = SteerableBody(world.createBody(bodyDef))

    private val texture = Texture("sheep.png")

    private val sprite = Sprite(texture).apply {
        setSize(SIZE, SIZE)
        setOriginCenter()
    }

    init {
        val circleShape = CircleShape().apply {
            radius = SIZE / 2
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

    fun draw(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    override fun dispose() {
        texture.dispose()
        world.destroyBody(steerableBody.body)
        disposed = true
    }

    fun setPosition(x: Float, y: Float) {
        steerableBody.body.setTransform(x, y, 0f)
    }

    fun shootInDirection(vector: Vector2) {
        steerableBody.body.setLinearVelocity(vector.x * SPEED, vector.y * SPEED)
    }

}