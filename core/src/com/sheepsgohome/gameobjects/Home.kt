package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.SteerableBody
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT

class Home(world: World) {

    companion object {
        val HOME_SIZE = 12f
    }

    private val texture = Texture("home.png")
    private val steerableBody: SteerableBody

    val sprite = Sprite(texture).apply {
        setSize(HOME_SIZE, HOME_SIZE)
        setPosition(
                -width / 2,
                CAMERA_HEIGHT / 2 - height
        )
    }

    init {
        val bodyDef = BodyDef().apply {
            type = StaticBody
            position.set(0f, 0f)
        }

        steerableBody = SteerableBody(world.createBody(bodyDef))

        val boxShape = PolygonShape().apply {
            setAsBox(HOME_SIZE / 2, HOME_SIZE / 2)
        }

        val fixtureDef = FixtureDef().apply {
            shape = boxShape
            density = 0.1f
            friction = 0.1f
            restitution = 0.6f
        }

        with(steerableBody) {
            body.createFixture(fixtureDef)
            body.userData = this
        }

        boxShape.dispose()
    }

    private fun updateSpritePosition() {
        with(steerableBody.body.position) {
            sprite.setPosition(x - HOME_SIZE / 2, y - HOME_SIZE / 2)
        }
    }

    fun positionTopCenter() {
        with(steerableBody) {
            body.setTransform(0f, CAMERA_HEIGHT / 2 - HOME_SIZE / 2, 0f)
        }

        updateSpritePosition()
    }


    fun draw(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    fun dispose() {
        texture.dispose()
    }
}