package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData

private val ALPHA_WOLF_SIZE = 10f
private val ALPHA_WOLF_SPEED = 1.2f

class AlphaWolf(world: World, val sheep: Sheep) : Wolf(
        world,
        ALPHA_WOLF_SIZE,
        FixtureDef().apply {
            density = 0.3f
            friction = 0.1f
            restitution = 0.6f
        }
) {


    private val texture = Texture("wolf-alpha.png")

    override val sprite = Sprite(texture).apply {
        setSize(ALPHA_WOLF_SIZE, ALPHA_WOLF_SIZE)
        setOriginCenter()
    }

    fun updateVelocity() {
        val sheepPosition = sheep.steerableBody.position

        val vec = Vector2(sheepPosition).sub(body.position).nor()
        val alphaSpeed = ALPHA_WOLF_SPEED / Math.abs(body.position.dst(sheepPosition) / GameData.CAMERA_HEIGHT)

        body.setLinearVelocity(vec.x * alphaSpeed, vec.y * alphaSpeed)
    }

    override fun updateSprite() = updateSprite(body, ALPHA_WOLF_SIZE)

    override fun transformBody(x: Float, y: Float, angle: Float) = transformBody(body, x, y, angle)

    override fun dispose() {
        texture.dispose()
    }

}