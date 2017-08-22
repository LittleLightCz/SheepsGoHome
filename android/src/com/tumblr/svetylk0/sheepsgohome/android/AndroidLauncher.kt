package com.tumblr.svetylk0.sheepsgohome.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.sheepsgohome.SheepsGoHomeMain
import com.sheepsgohome.shared.GameData
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.AndroidBridge
import com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge.LeaderboardBridge

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        val googleClient = GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .build()

        GameData.android = AndroidBridge(this)
        GameData.leaderboard = LeaderboardBridge(googleClient)

        initialize(SheepsGoHomeMain(), config)
    }

}
