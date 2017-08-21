package com.sheepsgohome

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body


open class SteerableBody(val body: Body) : Steerable<Vector2> {

    private val boundingRadius = 1f
    private var speed = 10f
    private var angularSpeed = 10f
    private var tagged = false

    private val steeringBehaviour = PrioritySteering(this).apply {
        isEnabled = true
    }

    protected var steeringAcceleration = SteeringAcceleration(Vector2())

    fun addSteeringBehaviour(behavior: SteeringBehavior<Vector2>) {
        behavior.isEnabled = true
        steeringBehaviour.add(behavior)
    }

    open fun calculateSteeringBehaviour() {
        steeringBehaviour.calculateSteering(steeringAcceleration)
        body.linearVelocity = steeringAcceleration.linear
    }

    override fun getPosition(): Vector2 = body.position

    override fun getOrientation() = body.angle

    override fun getLinearVelocity(): Vector2 = body.linearVelocity

    override fun getAngularVelocity() = body.angularVelocity

    override fun getBoundingRadius() = boundingRadius

    override fun isTagged() = tagged

    override fun setTagged(tagged: Boolean) {
        this.tagged = tagged
    }

    override fun newVector() = Vector2()

    override fun vectorToAngle(vector: Vector2) = vector.angle()

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        outVector.x = -Math.sin(angle.toDouble()).toFloat()
        outVector.y = Math.cos(angle.toDouble()).toFloat()
        return outVector
    }

    override fun getMaxLinearSpeed() = speed

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        speed = maxLinearSpeed
    }

    override fun getMaxLinearAcceleration() = speed

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {}

    override fun getMaxAngularSpeed() = angularSpeed

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        angularSpeed = maxAngularSpeed
    }

    override fun getMaxAngularAcceleration() = angularSpeed

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {}

}
