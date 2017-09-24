package com.sheepsgohome.google

import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult

interface GoogleLeaderboard {

    fun connect()

    fun cancelPendingResult()

    fun registerConnectionCallback(callback: GoogleConnectionCallback)
    fun unregisterConnectionCallback(callback: GoogleConnectionCallback)

    fun fetchLeaderboardData(onResultAction: (LeaderBoardResult) -> Unit)
}