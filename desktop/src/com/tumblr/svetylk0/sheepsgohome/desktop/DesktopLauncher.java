package com.tumblr.svetylk0.sheepsgohome.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sheepsgohome.SheepsGoHomeMain;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1080 / 3;
        config.height = 1920 / 3;
        new LwjglApplication(new SheepsGoHomeMain(new DesktopFunctions()), config);
    }
}
