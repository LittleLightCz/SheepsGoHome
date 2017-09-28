package com.tumblr.svetylk0.sheepsgohome.android

import android.content.Intent
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.sheepsgohome.SheepsGoHomeMain
import com.sheepsgohome.shared.GameData
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.AndroidBridge
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.GoogleClient
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.GoogleLeaderboardBridge
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.GoogleMultiplayerBridge

class AndroidLauncher : AndroidApplication() {

    lateinit var leaderboardBridge: GoogleLeaderboardBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        GoogleClient.activity = this

        leaderboardBridge = GoogleLeaderboardBridge()

        GameData.android = AndroidBridge(this)
        GameData.leaderboard = leaderboardBridge
        GameData.multiplayer = GoogleMultiplayerBridge(this)

        initialize(SheepsGoHomeMain(), config)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            LEADERBOARD_RESOLVE_CONNECTION_ISSUE -> GoogleClient.onActivityResult(requestCode, resultCode)
        }
    }

    companion object {
        val LEADERBOARD_RESOLVE_CONNECTION_ISSUE = 0
    }
}
