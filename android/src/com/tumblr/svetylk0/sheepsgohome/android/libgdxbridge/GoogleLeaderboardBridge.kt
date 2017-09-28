package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.games.Games
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult
import com.sheepsgohome.google.GoogleLeaderboard
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.leaderboard.LeaderBoardRow
import com.sheepsgohome.shared.GameData

class GoogleLeaderboardBridge : GoogleLeaderboard {

    companion object {
        val REQUEST_RESOLVE_CONNECTION_ISSUE = 0
    }

    private val CLASSIC_MODE_LEADERBOARD_ID = "CgkIzpjQtpcDEAIQAQ"

    private var pendingResult: PendingResult<LoadScoresResult>? = null

    private var callback: GoogleConnectionCallback? = null

    override fun connect() {
        GoogleClient.connect {
            callback?.onConnecting()
        }
    }

    override fun cancelPendingResult() {
        pendingResult?.cancel()
    }

    override fun registerConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = callback
        GoogleClient.addCallback(callback)
    }

    override fun unregisterConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = null
        GoogleClient.removeCallback(callback)
    }

    override fun fetchLeaderboardData(onResultAction: (LeaderBoardResult) -> Unit) {

        //Submit score first
        Games.Leaderboards.submitScoreImmediate(
                GoogleClient.get(),
                CLASSIC_MODE_LEADERBOARD_ID,
                GameData.LEVEL.toLong(),
                ""
        ).setResultCallback {
            //load my own score/leaderboard rank
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                    GoogleClient.get(),
                    CLASSIC_MODE_LEADERBOARD_ID,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC
            ).setResultCallback { myGoogleResult: LoadPlayerScoreResult? ->
                //then fetch the leaderboard page
                pendingResult = Games.Leaderboards.loadPlayerCenteredScores(
                        GoogleClient.get(),
                        CLASSIC_MODE_LEADERBOARD_ID,
                        LeaderboardVariant.TIME_SPAN_ALL_TIME,
                        LeaderboardVariant.COLLECTION_PUBLIC,
                        25,
                        true
                ).apply {
                    setResultCallback { scoresResult ->
                        val scores = scoresResult.scores.map {
                            score -> LeaderBoardRow(score.rank, score.scoreHolderDisplayName, score.rawScore)
                        }

                        val myResult = myGoogleResult?.score?.let {
                            LeaderBoardRow(it.rank, it.scoreHolderDisplayName, it.rawScore)
                        }

                        onResultAction(LeaderBoardResult(myResult, scores))
                    }
                }
            }
        }

    }

}