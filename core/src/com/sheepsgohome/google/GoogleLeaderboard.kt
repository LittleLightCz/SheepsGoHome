package com.sheepsgohome.google

import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback

interface GoogleLeaderboard {

    val isConnected: Boolean

    fun connect()

    fun registerConnectionCallback(callback: GoogleConnectionCallback)
}