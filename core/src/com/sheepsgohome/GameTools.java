package com.sheepsgohome;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by LittleLight on 10.1.2015.
 */
public class GameTools {
    public static Random random = new Random();


    private static float getRandomFloat() {
        return (random.nextFloat() * 2f) - 1f;
    }

    public static void setRandomMovement(Body b, float speed) {
        b.setLinearVelocity(getRandomFloat() * speed,getRandomFloat() * speed);
    }

    public static float calculateAngle(Vector2 v) {
        return calculateAngle(v.x, v.y);
    }


    public static float calculateAngle(float targetx, float targety) {
        float angle = (float) Math.toDegrees(Math.atan2(targety, targetx));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }


}
