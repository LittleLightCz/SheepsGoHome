package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.games.Games
import com.sheepsgohome.google.GoogleLeaderboard
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback

class GoogleLeaderboardBridge(val activity: Activity) : GoogleLeaderboard {

    companion object {
        val REQUEST_RESOLVE_CONNECTION_ISSUE = 0
    }

    private val client = GoogleApiClient.Builder(activity)
//            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener { it.startResolutionForResult(activity, REQUEST_RESOLVE_CONNECTION_ISSUE) }
            .addApi(Games.API)
            .addScope(Games.SCOPE_GAMES)
            .build()

    override val isConnected: Boolean
        get() = client.isConnected

    override fun connect() {
        client.connect()
    }

    override fun addConnectionCallback(callback: GoogleConnectionCallback) {
        client.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(p0: Bundle?) {
                callback.onConnected()
            }

            override fun onConnectionSuspended(p0: Int) {
                callback.onConnectionFailure()
            }
        })
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_RESOLVE_CONNECTION_ISSUE -> {
                if (resultCode == Activity.RESULT_OK) {
                    //reconnect
                    client.connect()
                } else {
                    //fire failure callback
//todo                    onConnectionSuspended(0)
                }
            }

        }
    }
}