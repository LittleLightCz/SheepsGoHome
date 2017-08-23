package com.sheepsgohome.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.gwt.GwtApplication
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration
import com.sheepsgohome.SheepsGoHomeMain
import com.sheepsgohome.android.Android
import com.sheepsgohome.shared.GameData

class HtmlLauncher : GwtApplication(), Android {

    override fun getConfig(): GwtApplicationConfiguration {
        return GwtApplicationConfiguration(480, 320)
    }

    override fun getApplicationListener(): ApplicationListener {
        GameData.android = this
        return SheepsGoHomeMain()
    }

    override fun launchRateAppAction() {

    }

    val deviceId: String?
        get() = null

    val countryCode: String?
        get() = null
}