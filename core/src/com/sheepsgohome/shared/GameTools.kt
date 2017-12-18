package com.sheepsgohome.shared

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.sheepsgohome.steerable.SteerableBody
import java.util.*

object GameTools {
    var random = Random()


    private val randomFloat: Float
        get() = random.nextFloat() * 2f - 1f

    fun calculateAngle(v: Vector2) = calculateAngle(v.x, v.y)


    fun calculateAngle(targetx: Float, targety: Float): Float {
        var angle = Math.toDegrees(Math.atan2(targety.toDouble(), targetx.toDouble())).toFloat()

        if (angle < 0) {
            angle += 360f
        }

        return angle
    }

    fun vectorAngleRadians(vec: Vector2) = vectorAngleRadians(vec.x, vec.y)

    fun vectorAngleRadians(x: Float, y: Float) = Math.toRadians(calculateAngle(x, y).toDouble()).toFloat()


    fun updateSpriteAccordingToBody(sprite: Sprite, spriteSize: Float, steerableBody: SteerableBody) {
        with(steerableBody.position) {
            sprite.setPosition(x - spriteSize / 2, y - spriteSize / 2)
        }

        sprite.rotation = Math.toDegrees(steerableBody.body.angle.toDouble()).toFloat()
    }

}

