package com.sheepsgohome;

import com.badlogic.gdx.Gdx;

/**
 * Created by LittleLight on 4.1.2015.
 */
public class Timer {
    private long interval;
    private long delta;

    public Timer(long interval) {
        this.delta = 0;
        this.interval = interval;
    }

    boolean intervalReached() {
        long tmp = System.currentTimeMillis() - delta;
        if (tmp > interval) {
            delta = tmp;
            return true;
        }

        return false;
    }

}
