package com.sheepsgohome.google

import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback

interface GoogleMultiplayer {
    fun connect()

    fun registerConnectionCallback(callback: GoogleConnectionCallback)
    fun unregisterConnectionCallback(callback: GoogleConnectionCallback)

    fun create()
    fun search()
}