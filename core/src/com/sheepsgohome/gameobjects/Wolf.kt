package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.*
import com.sheepsgohome.GameTools

abstract class Wolf(
        world: World,
        wolfSize: Float,
        fixture: FixtureDef
) {


    abstract val sprite: Sprite

    abstract fun updateSprite()

    fun draw(batch: SpriteBatch) = sprite.draw(batch)

    protected val body: Body

    init {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set(0f, 0f)
        }

        fixture.shape = CircleShape().apply {
            radius = wolfSize / 2 * 0.85f
        }

        body = world.createBody(bodyDef)
        body.userData = this
        body.createFixture(fixture)

        fixture.shape.dispose()
    }


    protected fun createWolfBody(): Body {


        return body
    }

    protected fun updateSprite(body: Body, wolfSize: Float) {
        with(body.position) {
            sprite.setPosition(
                    x - wolfSize / 2,
                    y - wolfSize / 2
            )
        }

        sprite.rotation = GameTools.calculateAngle(body.linearVelocity)
    }

    abstract fun transformBody(x: Float, y: Float, angle: Float)

    protected fun transformBody(body: Body, x: Float, y: Float, angle: Float) {
        body.setTransform(x, y, angle)
    }

    abstract fun dispose()

}