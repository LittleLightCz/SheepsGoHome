package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.realtime.*
import com.sheepsgohome.google.GoogleMultiplayer
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.tumblr.svetylk0.sheepsgohome.android.ActivityResult


class GoogleMultiplayerBridge(val activity: Activity) : GoogleMultiplayer, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener {

    private var callback: GoogleConnectionCallback? = null

    override fun connect() {
        GoogleClient.connect {
            callback?.onConnecting()
        }
    }

    override fun registerConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = callback
        GoogleClient.addCallback(callback)
    }

    override fun unregisterConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = null
        GoogleClient.removeCallback(callback)
    }

    override fun waitingRoomResult(resultCode: Int) {
        callback?.onOperationAborted()
    }

    override fun create() {
        val criteria = RoomConfig.createAutoMatchCriteria(1, 7, 0)

        val builder = RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this)
                .setAutoMatchCriteria(criteria)

        val roomConfig = builder.build()
        Games.RealTimeMultiplayer.create(GoogleClient.get(), roomConfig)
    }

    override fun search() {

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
        activity.startActivityForResult(intent, ActivityResult.MULTIPLAYER_WAITING_ROOM_INTENT)
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