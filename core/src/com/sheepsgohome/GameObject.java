package com.sheepsgohome;

/**
 * Created by LittleLight on 3.1.2015.
 */
public class GameObject {
    private int x;
    private int y;
    private int speed;
    private float rotation;

    private float angles[] = {0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f};

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 1;
        this.rotation = 0f;
    }

    protected GameObject() {
        this(0, 0);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveTo(int NewX, int NewY) {
        moveToOnALine(this.x,this.y, NewX, NewY, 2);
        calculateRotation(NewX,NewY);
    }

    private void moveToOnALine(int x,int y,int x2, int y2, int maxSteps) {
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
//            this.calculateRotation(x,y);


            this.x = x;
            this.y = y;

            if (maxSteps > 0) {
                maxSteps--;
            } else {
                return;
            }

            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }

    private void calculateRotation(int NewX, int NewY) {
            float angle = (float) Math.toDegrees(Math.atan2(NewY - y, NewX - x));

            if(angle < 0){
                angle += 360;
            }

           rotation = angle - 90;
    }

//    private void matchRotationAngle(float angle) {
//        float diff = Float.MAX_VALUE;
//        float newdiff;
//
//        for (float a: angles) {
//            newdiff = Math.abs(angle-a);
//            if (newdiff < diff) {
//                diff = newdiff;
//                rotation = a;
//            }
//        }
//    }

    public float getRotation() {
        return rotation;
    }
}
