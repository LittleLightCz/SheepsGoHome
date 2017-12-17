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
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameTools
import com.sheepsgohome.steerable.SteerableBody

open class Sheep(world: World) : Disposable {

    protected val SHEEP_SIZE = 6f
    private val SHEEP_SPEED = 20f

    private val bodyDef = BodyDef().apply {
        type = DynamicBody
        position.set(0f, 0f)
    }

    val steerableBody = SteerableBody(world.createBody(bodyDef))

    private val texture = Texture("sheep.png")

    protected val sprite = Sprite(texture).apply {
        setSize(SHEEP_SIZE, SHEEP_SIZE)
        setOriginCenter()
    }

    val yPosition
        get() = sprite.y

    init {
        val circleShape = CircleShape().apply {
            radius = SHEEP_SIZE / 2
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
            sprite.setPosition(x - SHEEP_SIZE / 2, y - SHEEP_SIZE / 2)
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