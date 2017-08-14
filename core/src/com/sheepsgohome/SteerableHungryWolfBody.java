package com.sheepsgohome;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;

public class SteerableHungryWolfBody extends SteerableBody {

    private final Pursue<Vector2> steeringPursue;
    private final FollowPath steeringFollowPath;

    private enum State {
        Wander, Hunt
    }

    private final SteerableBody sheep;
    private State state = State.Wander;

    private float huntDistance = 70f;

    public SteerableHungryWolfBody(Body wolfBody, SteerableBody sheep, SteerableBody home) {
        super(wolfBody);
        this.sheep = sheep;

        Array<Vector2> waypoints = new Array<Vector2>();
        generateWaypoints(waypoints, 5);
        LinePath<Vector2> linePath = new LinePath<Vector2>(waypoints);
        steeringFollowPath = new FollowPath<Vector2, LinePath.LinePathParam>(this, linePath);
        steeringFollowPath.setEnabled(true);
        steeringFollowPath.setPathOffset(10);

        steeringPursue = new Pursue<Vector2>(this, sheep);
        steeringPursue.setEnabled(true);

        setMaxLinearAcceleration(GameData.HUNGRY_WOLF_SPEED);
        setMaxLinearSpeed(GameData.HUNGRY_WOLF_SPEED);
    }

    private void generateWaypoints(Array<Vector2> waypoints, int count) {
        int failures = 0;

        float minimumDistance = 20;

        for (int i = 0; i < count; i++) {
            if (waypoints.size == 0) {
                waypoints.add(generateRandomVector());
            } else {
                Vector2 vec = generateRandomVector();
                while (findMinimumDistance(waypoints, vec) < minimumDistance) {
                    failures++;
                    if (failures > 5) {
                        break;
                    }
                }
                waypoints.add(vec);
            }
        }
    }

    private float findMinimumDistance(Array<Vector2> waypoints, Vector2 vec) {
        float ret = Float.MAX_VALUE;
        for (Vector2 arrVec : waypoints) {
            float dist = arrVec.dst(vec);
            if (dist < ret) {
                ret = dist;
            }
        }

        return ret;
    }

    private Vector2 generateRandomVector() {
        Vector2 vec = new Vector2();
        vec.x = (float) (((Math.random() * CAMERA_WIDTH)) - (CAMERA_WIDTH / 2f));
        vec.y = (float) (((Math.random() * CAMERA_HEIGHT)) - (CAMERA_HEIGHT / 2f));
        return vec;
    }

    @Override
    public void calculateSteeringBehaviour() {

        float distance = body.getPosition().dst(sheep.getPosition());
        if (distance < huntDistance) {
            steeringPursue.setMaxPredictionTime(distance / 40f);
            steeringPursue.calculateSteering(steeringAcceleration);

        } else {
            steeringFollowPath.calculateSteering(steeringAcceleration);
        }

        body.setLinearVelocity(steeringAcceleration.linear);
//
//        switch (state) {
//            case Hunt:
//                break;
//            case Attack:
//                steeringSeek.calculateSteering(steeringAcceleration);
//                break;
//            default:
////                steeringEvade.calculateSteering(steeringAcceleration);
//
//        }

    }
}
