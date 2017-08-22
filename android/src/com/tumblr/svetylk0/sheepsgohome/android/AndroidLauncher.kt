package com.tumblr.svetylk0.sheepsgohome.android

import android.content.Intent
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.sheepsgohome.SheepsGoHomeMain
import com.sheepsgohome.shared.GameData
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.AndroidBridge
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.GoogleLeaderboardBridge

class AndroidLauncher : AndroidApplication() {

    lateinit var leaderboardBridge: GoogleLeaderboardBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        leaderboardBridge = GoogleLeaderboardBridge(this)

        GameData.android = AndroidBridge(this)
        GameData.leaderboard = leaderboardBridge

        initialize(SheepsGoHomeMain(), config)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        leaderboardBridge.onActivityResult(requestCode, resultCode, data)
    }
}
