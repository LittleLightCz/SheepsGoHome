package com.sheepsgohome.gameobjects

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody

abstract class Wolf(private val world: World) {

    abstract val fixtureDef: FixtureDef

    private val bodyDef = BodyDef().apply {
        type = DynamicBody
        position.set(0f, 0f)
    }

    protected fun initializeWolfBody(): Body {
        val body = world.createBody(bodyDef)
        body.userData = this
        body.createFixture(fixtureDef)

        fixtureDef.shape.dispose()

        return body
    }

}