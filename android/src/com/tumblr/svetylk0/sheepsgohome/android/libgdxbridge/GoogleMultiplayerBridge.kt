package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.realtime.*
import com.sheepsgohome.google.GoogleMultiplayer


class GoogleMultiplayerBridge : GoogleMultiplayer, GoogleApiClient.ConnectionCallbacks, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener {



    override fun createRoom() {
        val builder = RoomConfig.builder(this)
        builder.setMessageReceivedListener(this)
        builder.setRoomStatusUpdateListener(this)

        val roomConfig = builder.build()
        Games.RealTimeMultiplayer.create(GoogleClient.get(), roomConfig)
    }

    /**
     * Google client callbacks
     */

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    /**
     * RoomUpdateListener callbacks
     */

    override fun onJoinedRoom(p0: Int, p1: Room?) {
    }

    override fun onLeftRoom(p0: Int, p1: String?) {
    }

    override fun onRoomCreated(p0: Int, p1: Room?) {
    }

    override fun onRoomConnected(p0: Int, p1: Room?) {
    }

    /**
     * RealTimeMessageReceivedListener callback
     */

    override fun onRealTimeMessageReceived(p0: RealTimeMessage?) {
    }

    /**
     * RoomStatusUpdateListener callbacks
     */

    override fun onRoomConnecting(p0: Room?) {
    }

    override fun onP2PConnected(p0: String?) {
    }

    override fun onDisconnectedFromRoom(p0: Room?) {
    }

    override fun onPeerDeclined(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onPeersConnected(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onPeerInvitedToRoom(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onPeerLeft(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onRoomAutoMatching(p0: Room?) {
    }

    override fun onPeerJoined(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onConnectedToRoom(p0: Room?) {
    }

    override fun onPeersDisconnected(p0: Room?, p1: MutableList<String>?) {
    }

    override fun onP2PDisconnected(p0: String?) {
    }

}