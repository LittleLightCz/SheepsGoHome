package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.sheepsgohome.android.Android

class AndroidBridge(val activity: Activity) : Android {

    override fun launchRateAppAction() {
        val packageName = "com.tumblr.svetylk0.sheepsgohome.android"
        val uri = Uri.parse("market://details?id=" + packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

        try {
            activity.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
        }
    }
}