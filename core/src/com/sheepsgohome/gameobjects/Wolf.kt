package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.*
import com.sheepsgohome.shared.GameTools

open class Wolf(
        world: World,
        fixture: FixtureDef,
        private val wolfSize: Float,
        private val texture: Texture
) {

    val body: Body

    private val sprite = Sprite(texture).apply {
        setSize(wolfSize, wolfSize)
        setOriginCenter()
    }

    init {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set(0f, 0f)
        }

        fixture.shape = CircleShape().apply {
            radius = wolfSize / 2 * 0.85f
        }

        body = world.createBody(bodyDef)
        body.createFixture(fixture)

        fixture.shape.dispose()
    }

    fun updateSprite() {
        with(body.position) {
            sprite.setPosition(
                    x - wolfSize / 2,
                    y - wolfSize / 2
            )
        }

        sprite.rotation = GameTools.calculateAngle(body.linearVelocity)
    }

    fun draw(batch: SpriteBatch) = sprite.draw(batch)

    fun dispose() {
        texture.dispose()
    }

}