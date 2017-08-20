package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.GameTools

abstract class Wolf(private val world: World) {

    abstract val fixtureDef: FixtureDef
    abstract val sprite: Sprite

    abstract fun updateSprite()

    fun draw(batch: SpriteBatch) = sprite.draw(batch)

    private val bodyDef = BodyDef().apply {
        type = DynamicBody
        position.set(0f, 0f)
    }

    protected fun createWolfBody(): Body {
        val body = world.createBody(bodyDef)
        body.userData = this
        body.createFixture(fixtureDef)

        fixtureDef.shape.dispose()

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