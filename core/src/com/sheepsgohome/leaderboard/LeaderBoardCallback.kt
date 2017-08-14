package com.sheepsgohome.leaderboard

interface LeaderBoardCallback {

    fun connecting()

    fun connectionToDatabaseFailed()

    fun invalidData()

    fun nickAlreadyInUse()

    fun success()

    fun failure()

    fun failedToInitializeMD5()

    fun connectionFailed()

    fun connectionCanceled()

    fun unregisteredUser()

    fun leaderboardResult(result: LeaderBoardResult)
}
