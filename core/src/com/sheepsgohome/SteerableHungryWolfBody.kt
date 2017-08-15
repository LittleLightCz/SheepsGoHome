package com.sheepsgohome

import com.badlogic.gdx.ai.steer.behaviors.FollowPath
import com.badlogic.gdx.ai.steer.behaviors.Pursue
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Array

import com.sheepsgohome.GameData.CAMERA_HEIGHT
import com.sheepsgohome.GameData.CAMERA_WIDTH

class SteerableHungryWolfBody(wolfBody: Body, private val sheep: SteerableBody, home: SteerableBody) : SteerableBody(wolfBody) {

    private val steeringPursue: Pursue<Vector2>
    private val steeringFollowPath: FollowPath<Vector2, LinePath.LinePathParam>

    private enum class State {
        Wander, Hunt
    }

    private val state = State.Wander

    private val huntDistance = 70f

    init {
        val waypoints = Array<Vector2>()
        generateWaypoints(waypoints, 5)

        val linePath = LinePath(waypoints)
        steeringFollowPath = FollowPath(this, linePath)
        steeringFollowPath.isEnabled = true
        steeringFollowPath.pathOffset = 10f

        steeringPursue = Pursue(this, sheep)
        steeringPursue.isEnabled = true

        maxLinearAcceleration = GameData.HUNGRY_WOLF_SPEED
        maxLinearSpeed = GameData.HUNGRY_WOLF_SPEED
    }

    private fun generateWaypoints(waypoints: Array<Vector2>, count: Int) {
        var failures = 0

        val minimumDistance = 20f

        for (i in 0..count - 1) {
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
    }

    private fun findMinimumDistance(waypoints: Array<Vector2>, vec: Vector2): Float {
        var ret = java.lang.Float.MAX_VALUE
        for (arrVec in waypoints) {
            val dist = arrVec.dst(vec)
            if (dist < ret) {
                ret = dist
            }
        }

        return ret
    }

    private fun generateRandomVector(): Vector2 {
        val vec = Vector2()
        vec.x = (Math.random() * CAMERA_WIDTH - CAMERA_WIDTH / 2f).toFloat()
        vec.y = (Math.random() * CAMERA_HEIGHT - CAMERA_HEIGHT / 2f).toFloat()
        return vec
    }

    override fun calculateSteeringBehaviour() {

        val distance = body.position.dst(sheep.position)
        if (distance < huntDistance) {
            steeringPursue.maxPredictionTime = distance / 40f
            steeringPursue.calculateSteering(steeringAcceleration)

        } else {
            steeringFollowPath.calculateSteering(steeringAcceleration)
        }

        body.linearVelocity = steeringAcceleration.linear
    }
}
