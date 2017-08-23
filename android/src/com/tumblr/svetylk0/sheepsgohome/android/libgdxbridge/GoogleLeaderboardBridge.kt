package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.games.Games
import com.sheepsgohome.google.GoogleLeaderboard
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback

class GoogleLeaderboardBridge(val activity: Activity) : GoogleLeaderboard, ConnectionCallbacks {

    companion object {
        val REQUEST_RESOLVE_CONNECTION_ISSUE = 0
    }

    private lateinit var callback: GoogleConnectionCallback

    private val client = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener { result ->
                if (result.hasResolution()) {
                    result.startResolutionForResult(activity, REQUEST_RESOLVE_CONNECTION_ISSUE)
                } else {
                    callback.onConnectionFailure()
                }
            }
            .addApi(Games.API)
            .addScope(Games.SCOPE_GAMES)
            .build()

    override val isConnected: Boolean
        get() = client.isConnected

    override fun connect() {
        client.connect()
    }


    override fun registerConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = callback
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_RESOLVE_CONNECTION_ISSUE -> {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        //reconnect
                        client.connect()
                    }
                    else -> onConnectionSuspended(resultCode)
                }
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        callback.onConnected()
    }

    override fun onConnectionSuspended(code: Int) {
        callback.onConnectionFailure()
    }
}