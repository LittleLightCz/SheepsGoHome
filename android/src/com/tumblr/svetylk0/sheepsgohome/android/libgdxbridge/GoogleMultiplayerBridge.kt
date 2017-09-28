package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.realtime.*
import com.sheepsgohome.google.GoogleMultiplayer


class GoogleMultiplayerBridge(val activity: Activity) : GoogleMultiplayer, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener {
    override fun connect() {
        GoogleClient.connect {
            //TODO connecting?
        }
    }

    override fun createRoom() {

        val criteria = RoomConfig.createAutoMatchCriteria(1, 7, 0)

        val builder = RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this)
                .setAutoMatchCriteria(criteria)

        val roomConfig = builder.build()
        Games.RealTimeMultiplayer.create(GoogleClient.get(), roomConfig)
    }

    /**
     * RoomUpdateListener callbacks
     */

    override fun onJoinedRoom(p0: Int, p1: Room?) {
    }

    override fun onLeftRoom(p0: Int, p1: String?) {
    }

    override fun onRoomCreated(status: Int, room: Room?) {
        val intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(GoogleClient.get(), room, 1)
        activity.startActivityForResult(intent, 0)
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