package com.tumblr.svetylk0.sheepsgohome.android.libgdxbridge

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
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

class GoogleLeaderboardBridge(val activity: Activity) : GoogleLeaderboard, ConnectionCallbacks {

    companion object {
        val REQUEST_RESOLVE_CONNECTION_ISSUE = 0
    }

    private val CLASSIC_MODE_LEADERBOARD_ID = "CgkIzpjQtpcDEAIQAQ"

    private var callback: GoogleConnectionCallback? = null
    private var pendingResult: PendingResult<LoadScoresResult>? = null

    private val client = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener { result ->
                if (result.hasResolution()) {
                    result.startResolutionForResult(activity, REQUEST_RESOLVE_CONNECTION_ISSUE)
                } else {
                    callback?.onConnectionFailure()
                }
            }
            .addApi(Games.API)
            .addScope(Games.SCOPE_GAMES)
            .build()

    override val isConnected: Boolean
        get() = client.isConnected

    override fun connect() {
        client.connect()
    }

    override fun cancelPendingResult() {
        pendingResult?.cancel()
    }

    override fun registerConnectionCallback(callback: GoogleConnectionCallback) {
        this.callback = callback
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        when(requestCode) {
            REQUEST_RESOLVE_CONNECTION_ISSUE -> {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        //reconnect
                        client.connect()
                    }
                    else ->  onConnectionSuspended(resultCode)
                }
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        callback?.onConnected()
    }

    override fun onConnectionSuspended(code: Int) {
        callback?.onConnectionFailure()
    }

    override fun fetchLeaderboardData(onResultAction: (LeaderBoardResult) -> Unit) {

        //Submit score first
        Games.Leaderboards.submitScoreImmediate(
                client,
                CLASSIC_MODE_LEADERBOARD_ID,
                GameData.LEVEL.toLong(),
                ""
        ).setResultCallback {
            //load my own score/leaderboard rank
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(client,
                    CLASSIC_MODE_LEADERBOARD_ID,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC
            ).setResultCallback { myGoogleResult: LoadPlayerScoreResult? ->
                //then fetch the leaderboard page
                pendingResult = Games.Leaderboards.loadPlayerCenteredScores(
                        client,
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