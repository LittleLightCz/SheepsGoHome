package com.tumblr.svetylk0.sheepsgohome.android

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.sheepsgohome.SheepsGoHomeMain
import com.sheepsgohome.interfaces.AndroidFunctions
import com.sheepsgohome.shared.GameData
import java.util.*

class AndroidLauncher : AndroidApplication(), AndroidFunctions {

    init {
        GameData.androidFunctions = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(SheepsGoHomeMain(), config)
    }

    override fun launchRateAppAction() {
        val packageName = "com.tumblr.svetylk0.sheepsgohome.android"
        val uri = Uri.parse("market://details?id=" + packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
        }
    }

    override val deviceId: String
        get() {
            val tm = baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val id: String? = tm.deviceId

            return when (id) {
                null, "" -> Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                else -> id
            }
        }

    override val countryCode: String
        get() {
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val country: String? = tm.simCountryIso

            return when (country) {
                null, "" -> Locale.getDefault().country
                else -> country
            }
        }
}
