package com.sheepsgohome.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World

abstract class Wall(world: World) {


    protected val body: Body

    init {
        val bodyDef = BodyDef().apply {
            position.set(0f, 0f)
            type = BodyDef.BodyType.StaticBody
        }

        val boxShape = PolygonShape().apply {
            setAsBox(0.1f, Math.max(Gdx.graphics.width, Gdx.graphics.height).toFloat())
        }

        body = world.createBody(bodyDef).apply {
            createFixture(boxShape, 0.0f)
            userData = this
        }

        boxShape.dispose()
    }


    abstract fun updatePosition()
}