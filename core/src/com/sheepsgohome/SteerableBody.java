package com.sheepsgohome;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


public class SteerableBody implements Steerable<Vector2> {

    protected Body body;
    private float boundingRadius;
    private float speed;
    private float angularSpeed;
    private boolean tagged;

    private PrioritySteering<Vector2> steeringBehaviour;
    protected SteeringAcceleration<Vector2> steeringAcceleration;

    public SteerableBody(Body wolfBody) {
        body = wolfBody;
        boundingRadius = 1;
        tagged = false;
        speed = 10f;
        angularSpeed = 10f;

        steeringBehaviour = new PrioritySteering<Vector2>(this);
        steeringBehaviour.setEnabled(true);

        steeringAcceleration = new SteeringAcceleration<Vector2>(new Vector2());
    }

    public void addSteeringBehaviour(SteeringBehavior<Vector2> behavior) {
        behavior.setEnabled(true);
        steeringBehaviour.add(behavior);
    }

    public void calculateSteeringBehaviour() {
        steeringBehaviour.calculateSteering(steeringAcceleration);
        body.setLinearVelocity(steeringAcceleration.linear);
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angle();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public float getMaxLinearSpeed() {
        return speed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        speed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return speed;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        //nope
    }

    @Override
    public float getMaxAngularSpeed() {
        return angularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        angularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return angularSpeed;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        //nope
    }

    public Object getUserData() {
        return body.getUserData();
    }

    public Body getBody() {
        return body;
    }
}
