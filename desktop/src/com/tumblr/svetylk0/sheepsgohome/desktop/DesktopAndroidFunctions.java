package com.tumblr.svetylk0.sheepsgohome.desktop;

import com.sheepsgohome.interfaces.AndroidFunctions;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by LittleLight on 28.2.2015.
 */
public class DesktopAndroidFunctions implements AndroidFunctions {

    @Override
    public void launchRateAppAction() {

    }

    @Override
    public String getDeviceId() {
        String hostname = "Unknown";

        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        return hostname;
    }

    @Override
    public String getCountryCode() {
        return System.getProperty("user.country");
    }


}
