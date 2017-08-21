package com.sheepsgohome.steerable

import com.badlogic.gdx.ai.steer.behaviors.FollowPath
import com.badlogic.gdx.ai.steer.behaviors.Pursue
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Array
import com.sheepsgohome.gameobjects.HungryWolf
import com.sheepsgohome.gameobjects.Sheep

import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

class SteerableHungryWolfBody(wolfBody: Body, private val sheep: Sheep) : SteerableBody(wolfBody) {

    private val steeringPursue = Pursue(this, sheep.steerableBody).apply {
        isEnabled = true
    }

    private val steeringFollowPath: FollowPath<Vector2, LinePath.LinePathParam>

    private val huntDistance = 70f

    init {
        val waypoints = generateWaypoints(5)

        val linePath = LinePath(waypoints)
        steeringFollowPath = FollowPath(this, linePath).apply {
            isEnabled = true
            pathOffset = 10f
        }

        maxLinearAcceleration = HungryWolf.HUNGRY_WOLF_SPEED
        maxLinearSpeed = HungryWolf.HUNGRY_WOLF_SPEED
    }

    private fun generateWaypoints(count: Int): Array<Vector2> {
        var failures = 0

        val waypoints = Array<Vector2>()
        val minimumDistance = 20f

        for (i in 1..count) {
            if (waypoints.size == 0) {
                waypoints.add(generateRandomVector())
            } else {
                val vec = generateRandomVector()
                while (findMinimumDistance(waypoints, vec) < minimumDistance) {
                    failures++
                    if (failures > 5) {
                        break
                    }
                }
                waypoints.add(vec)
            }
        }

        return waypoints
    }

    private fun findMinimumDistance(waypoints: Array<Vector2>, vec: Vector2): Float {
        return waypoints.asSequence()
                .map { it.dst(vec) }
                .min() ?: Float.MAX_VALUE
    }

    private fun generateRandomVector(): Vector2 {
        val vec = Vector2()
        vec.x = (Math.random() * CAMERA_WIDTH - CAMERA_WIDTH / 2f).toFloat()
        vec.y = (Math.random() * CAMERA_HEIGHT - CAMERA_HEIGHT / 2f).toFloat()
        return vec
    }

    override fun calculateSteeringBehaviour() {
        val sheepPosition = sheep.steerableBody.position

        val distance = body.position.dst(sheepPosition)
        if (distance < huntDistance) {
            //Hunt the sheep
            steeringPursue.maxPredictionTime = distance / 40f
            steeringPursue.calculateSteering(steeringAcceleration)
        } else {
            //Wander
            steeringFollowPath.calculateSteering(steeringAcceleration)
        }

        body.linearVelocity = steeringAcceleration.linear
    }
}
