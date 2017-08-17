package com.sheepsgohome.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sheepsgohome.SheepsGoHomeMain;
import com.sheepsgohome.interfaces.FunctionsInterface;
import com.sheepsgohome.shared.GameData;

public class HtmlLauncher extends GwtApplication implements FunctionsInterface {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        GameData.functions = this;
        return new SheepsGoHomeMain();
    }

    @Override
    public void launchRateAppAction() {

    }

    @Override
    public String getDeviceId() {
        return null;
    }

    @Override
    public String getCountryCode() {
        return null;
    }
}