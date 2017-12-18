package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.sheepsgohome.shared.GameData

open class Sheep(world: World) : SteerableGameObject(world), Disposable {

    protected val SHEEP_SIZE = 6f
    private val SHEEP_SPEED = 20f

    private val sheepTexture = Texture("sheep.png")

    protected val sheepSprite = Sprite(sheepTexture).apply {
        setSize(SHEEP_SIZE, SHEEP_SIZE)
        setOriginCenter()
    }

    val yPosition
        get() = sheepSprite.y

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

    open fun draw(batch: SpriteBatch) {
        sheepSprite.draw(batch)
    }

    override fun dispose() {
        sheepTexture.dispose()
    }

    open fun updateSprite() = updateSprite(sheepSprite, SHEEP_SIZE)

    fun positionBottomCenter() {
        with(steerableBody.body) {
            setTransform(0f, -GameData.CAMERA_HEIGHT / 2f + 2, (Math.PI / 2f).toFloat())
        }
    }

    fun updateVelocity(direction: Vector2) = updateBodyVelocity(direction, SHEEP_SPEED)

    fun updateVelocity(directionX: Float, directionY: Float) = updateBodyVelocity(directionX, directionY, SHEEP_SPEED)

}