package com.sheepsgohome

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body


open class SteerableBody(val body: Body) : Steerable<Vector2> {

    private val boundingRadius: Float
    private var speed: Float = 0.toFloat()
    private var angularSpeed: Float = 0.toFloat()
    private var tagged: Boolean = false

    private val steeringBehaviour: PrioritySteering<Vector2>

    protected var steeringAcceleration: SteeringAcceleration<Vector2>


    init {
        boundingRadius = 1f
        tagged = false
        speed = 10f
        angularSpeed = 10f

        steeringBehaviour = PrioritySteering(this)
        steeringBehaviour.isEnabled = true

        steeringAcceleration = SteeringAcceleration(Vector2())
    }

    fun addSteeringBehaviour(behavior: SteeringBehavior<Vector2>) {
        behavior.isEnabled = true
        steeringBehaviour.add(behavior)
    }

    open fun calculateSteeringBehaviour() {
        steeringBehaviour.calculateSteering(steeringAcceleration)
        body.linearVelocity = steeringAcceleration.linear
    }

    override fun getPosition(): Vector2 {
        return body.position
    }

    override fun getOrientation(): Float {
        return body.angle
    }

    override fun getLinearVelocity(): Vector2 {
        return body.linearVelocity
    }

    override fun getAngularVelocity(): Float {
        return body.angularVelocity
    }

    override fun getBoundingRadius(): Float {
        return boundingRadius
    }

    override fun isTagged(): Boolean {
        return tagged
    }

    override fun setTagged(tagged: Boolean) {
        this.tagged = tagged
    }

    override fun newVector(): Vector2 {
        return Vector2()
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return vector.angle()
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        outVector.x = -Math.sin(angle.toDouble()).toFloat()
        outVector.y = Math.cos(angle.toDouble()).toFloat()
        return outVector
    }

    override fun getMaxLinearSpeed(): Float {
        return speed
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        speed = maxLinearSpeed
    }

    override fun getMaxLinearAcceleration(): Float {
        return speed
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        //nope
    }

    override fun getMaxAngularSpeed(): Float {
        return angularSpeed
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        angularSpeed = maxAngularSpeed
    }

    override fun getMaxAngularAcceleration(): Float {
        return angularSpeed
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        //nope
    }

    val userData: Any
        get() = body.userData
}
