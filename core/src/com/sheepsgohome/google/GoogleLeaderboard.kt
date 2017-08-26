package com.sheepsgohome.google

import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult

interface GoogleLeaderboard {

    val isConnected: Boolean

    fun connect()

    fun cancelPendingResult()

    fun registerConnectionCallback(callback: GoogleConnectionCallback)

    fun fetchLeaderboardData(onResultAction: (LeaderBoardResult) -> Unit)
}