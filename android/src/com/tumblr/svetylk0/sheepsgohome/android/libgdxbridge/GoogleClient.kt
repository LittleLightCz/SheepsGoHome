package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.tumblr.svetylk0.sheepsgohome.android.AndroidLauncher

object GoogleClient : GoogleApiClient.ConnectionCallbacks {

    lateinit var activity: Activity

    private var callbacks = mutableListOf<GoogleConnectionCallback>()

    private val client: GoogleApiClient by lazy {
        GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener { result ->
                    if (result.hasResolution()) {
                        result.startResolutionForResult(activity, GoogleLeaderboardBridge.REQUEST_RESOLVE_CONNECTION_ISSUE)
                    } else {
                        callbacks.forEach { it.onConnectionFailure() }
                    }
                }
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build()
    }

    fun get() = client

    val isConnected: Boolean
        get() = client.isConnected

    fun connect(onConnecting: () -> Unit) {
        if (!isConnected) {
            client.connect()
            onConnecting()
        } else {
            onConnected(null)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        callbacks.forEach { it.onConnected() }
    }

    override fun onConnectionSuspended(code: Int) {
        callbacks.forEach { it.onConnectionFailure() }
    }

    fun addCallback(callback: GoogleConnectionCallback) {
        callbacks.add(callback)
    }

    fun removeCallback(callback: GoogleConnectionCallback) {
        callbacks = callbacks.filterNot { it === callback }.toMutableList()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        when (requestCode) {
            AndroidLauncher.LEADERBOARD_RESOLVE_CONNECTION_ISSUE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //reconnect
                        client.connect()
                    }
                    else -> onConnectionSuspended(resultCode)
                }
            }
        }
    }


}