package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.sheepsgohome.GameTools
import com.sheepsgohome.SteerableBody
import com.sheepsgohome.shared.GameData

class Sheep(world: World) : Disposable {

    private val SIZE = 6f
    private val SHEEP_SPEED = 20f

    private val bodyDef = BodyDef().apply {
        type = DynamicBody
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
            density = 0.1f
            friction = 0.1f
            restitution = 0.6f
        }

        steerableBody.body.createFixture(fixtureDef)
        steerableBody.body.userData = this

        circleShape.dispose()
    }

    fun updateSprite() {
        with(steerableBody.position) {
            sprite.setPosition(x - SIZE / 2, y - SIZE / 2)
        }

        sprite.rotation = Math.toDegrees(steerableBody.body.angle.toDouble()).toFloat()
    }

    fun updateVelocity(x: Float, y: Float) {
        steerableBody.body.setLinearVelocity(x * SHEEP_SPEED, y * SHEEP_SPEED)
        updateAngle(GameTools.vectorAngleRadians(x, y))
    }

    fun updateVelocity(vec: Vector2) = updateVelocity(vec.x, vec.y)

    private fun updateAngle(angleRadians: Float) {
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

    fun draw(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    override fun dispose() {
        texture.dispose()
    }

    fun positionBottomCenter() {
        with(steerableBody.body) {
            setTransform(0f, -GameData.CAMERA_HEIGHT / 2f + 2, (Math.PI / 2f).toFloat())
        }
    }


}