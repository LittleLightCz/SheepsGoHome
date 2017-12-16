package com.sheepsgohome.google.leaderboard

interface GoogleConnectionCallback {
    fun onConnected()
    fun onConnecting()
    fun onConnectionFailure()
    fun onOperationAborted()
}
