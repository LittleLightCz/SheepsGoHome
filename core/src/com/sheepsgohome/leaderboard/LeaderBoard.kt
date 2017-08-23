package com.sheepsgohome.leaderboard

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.HttpParametersUtils
import com.badlogic.gdx.utils.Json
import com.sheepsgohome.shared.GameData
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LeaderBoard {

    companion object {
        val RESPONSE_DB_CONNECTION_ERROR = "DB_CONNECTION_ERROR"
        val RESPONSE_INVALID_DATA = "INVALID_DATA"
        val RESPONSE_NICK_ALREADY_IN_USE = "NICK_ALREADY_IN_USE"
        val RESPONSE_SUCCESS = "SUCCESS"
        val RESPONSE_FAILURE = "FAILURE"
        val RESPONSE_UNREGISTERED_USER = "UNREGISTERED_USER"

        val instance by lazy { LeaderBoard() }
    }

    var isTerminated: Boolean = false
        @Synchronized get
        @Synchronized set

    fun fetchLeaderboard(id: String, callback: LeaderBoardCallback) {
        isTerminated = false

        val parameters = mapOf("id" to id)

        val httpPost = Net.HttpRequest(Net.HttpMethods.POST)
        httpPost.url = "http://sheepsgohome-com.svethostingu-tmp.cz/leaderboard/get.php"
        httpPost.content = HttpParametersUtils.convertHttpParameters(parameters)

        callback.connecting()

        Gdx.net.sendHttpRequest(httpPost, object : Net.HttpResponseListener {
            var status: String = ""

            override fun handleHttpResponse(httpResponse: Net.HttpResponse) {

                if (isTerminated)
                    return

                status = httpResponse.resultAsString

                //parse message
                val json = Json()
                val result = json.fromJson(LeaderBoardResult::class.java, status)

                if (result != null) {

//                    when(result.error) {
//                        "" -> callback.leaderboardResult(result)
//                        RESPONSE_DB_CONNECTION_ERROR -> callback.connectionToDatabaseFailed()
//                        RESPONSE_UNREGISTERED_USER -> callback.unregisteredUser()
//                        else -> callback.failure()
//                    }


                } else {
                    callback.failure()
                }

            }

            override fun failed(t: Throwable) {
                if (isTerminated)
                    return

                callback.connectionFailed()
            }

            override fun cancelled() {
                if (isTerminated)
                    return

                callback.connectionCanceled()
            }
        })
    }

    fun register(id: String, nick: String, level: Int, country: String, callback: LeaderBoardCallback) {
        isTerminated = false

        val parameters = mutableMapOf(
            "id" to id,
            "nick" to nick,
            "level" to level.toString(),
            "country" to country
        )

        try {
            val hashString = id + nick + level.toString() + country + GameData.codeX
            parameters.put("checksum", md5(hashString))
        } catch (e: NoSuchAlgorithmException) {
            callback.failedToInitializeMD5()
        }

        val httpPost = Net.HttpRequest(Net.HttpMethods.POST)
        httpPost.url = "http://sheepsgohome-com.svethostingu-tmp.cz/leaderboard/update.php"
        httpPost.content = HttpParametersUtils.convertHttpParameters(parameters)

        callback.connecting()

        Gdx.net.sendHttpRequest(httpPost, object : Net.HttpResponseListener {
            var status: String = ""

            override fun handleHttpResponse(httpResponse: Net.HttpResponse) {
                if (isTerminated)
                    return

                status = httpResponse.resultAsString

                //parse message
                val json = Json()
                val response = json.fromJson(String::class.java, status)

                when (response) {
                    RESPONSE_DB_CONNECTION_ERROR -> callback.connectionToDatabaseFailed()
                    RESPONSE_INVALID_DATA -> callback.invalidData()
                    RESPONSE_NICK_ALREADY_IN_USE -> callback.nickAlreadyInUse()
                    RESPONSE_SUCCESS -> callback.success()
                    RESPONSE_FAILURE -> callback.failure()
                    else -> callback.failure()
                }
            }

            override fun failed(t: Throwable) {
                if (isTerminated)
                    return

                callback.connectionFailed()
            }

            override fun cancelled() {
                if (isTerminated)
                    return

                callback.connectionCanceled()
            }
        })
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun md5(input: String?): String {
        var result: String = input ?: ""

        if (input != null) {
            val md = MessageDigest.getInstance("MD5") //or "SHA-1"
            md.update(input.toByteArray())
            val hash = BigInteger(1, md.digest())
            result = hash.toString(16)
            while (result.length < 32) { //40 for SHA-1
                result = "0" + result
            }
        }

        return result
    }

}

