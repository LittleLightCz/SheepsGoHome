package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.sheepsgohome.SteerableBody

class Sheep(world: World): Disposable {

    private val SIZE = 6f

    private val bodyDef = BodyDef().apply {
        type = DynamicBody
        position.set(0f, 0f)
    }

    val steerableBody = SteerableBody(world.createBody(bodyDef))

    val texture = Texture("sheep.png")

    val sprite = Sprite(texture).apply {
        setSize(SIZE, SIZE)
        setOriginCenter()
    }

    init {
        val circleShape = CircleShape().apply {
            radius = SIZE / 2
        }

        val fixtureDef = FixtureDef().apply {
            shape = circleShape
            density = 0.1f
            friction = 0.1f
            restitution = 0.6f
        }

        steerableBody.body.createFixture(fixtureDef)
        steerableBody.body.userData = this

        circleShape.dispose()
    }

    fun computePosition() {
        sprite.setPosition(
                steerableBody.position.x - SIZE / 2,
                steerableBody.position.y - SIZE / 2
        )
    }

    fun computeRotation() {
        sprite.rotation = Math.toDegrees(steerableBody.body.angle.toDouble()).toFloat()
    }

    fun resetVelocity() {
        steerableBody.body.setLinearVelocity(0f, 0f)
        steerableBody.body.angularVelocity = 0f
    }

    fun draw(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    override fun dispose() {
        texture.dispose()
    }

}