package com.tumblr.svetylk0.sheepsgohome.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sheepsgohome.SheepsGoHomeMain;
import com.sheepsgohome.interfaces.FunctionsInterface;

import java.util.Locale;

public class AndroidLauncher extends AndroidApplication implements FunctionsInterface{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SheepsGoHomeMain(this), config);
	}

    @Override
    public void launchDonateAction() {

    }

    @Override
    public void launchRateAppAction() {
        String packageName = "com.tumblr.svetylk0.sheepsgohome.android";
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    @Override
    public String getDeviceId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getDeviceId();

        if (id == null || id.equals("")) {
            id = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        return id;
    }

    @Override
    public String getCountryCode() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String country = tm.getSimCountryIso();

        if (country == null || country.equals("")) {
            country = Locale.getDefault().getCountry();
        }

        return country;
    }
}
