package com.sheepsgohome.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Json;
import com.sheepsgohome.GameData;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LittleLight on 22.3.2015.
 */
public class LeaderBoard {
    public static final String RESPONSE_DB_CONNECTION_ERROR = "DB_CONNECTION_ERROR";
    public static final String RESPONSE_INVALID_DATA = "INVALID_DATA";
    public static final String RESPONSE_NICK_ALREADY_IN_USE = "NICK_ALREADY_IN_USE";
    public static final String RESPONSE_SUCCESS = "SUCCESS";
    public static final String RESPONSE_FAILURE = "FAILURE";
    public static final String RESPONSE_UNREGISTERED_USER = "UNREGISTERED_USER";

    private boolean terminated;


    public void fetchLeaderboard(String id, final LeaderBoardCallback callback) {
        terminated = false;

        Map parameters = new HashMap();
        parameters.put("id", id);

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setUrl("http://sheepsgohome-com.svethostingu-tmp.cz/leaderboard/get.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        callback.connecting();

        Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
            public String status;

            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                if (isTerminated())
                    return;

                status = httpResponse.getResultAsString();

                //parse message
                Json json = new Json();
                LeaderBoardResult result = json.fromJson(LeaderBoardResult.class, status);

                if (result != null) {

                    if (result.getError().equals("")) {
                        callback.leaderboardResult(result);
                    } else if (result.getError().equals(RESPONSE_DB_CONNECTION_ERROR)) {
                        callback.connectionToDatabaseFailed();
                    } else if (result.getError().equals(RESPONSE_UNREGISTERED_USER)) {
                        callback.unregisteredUser();
                    } else {
                        callback.failure();
                    }


                } else {
                    callback.failure();
                }

            }

            public void failed(Throwable t) {
                if (isTerminated())
                    return;

                callback.connectionFailed();
            }

            @Override
            public void cancelled() {
                if (isTerminated())
                    return;

                callback.connectionCanceled();
            }
        });
    }


    public void register(String id, String nick, int level, String country, final LeaderBoardCallback callback) {
        terminated = false;

        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("nick", nick);
        parameters.put("level", String.valueOf(level));
        parameters.put("country", country);

        try {
            String hashString = id + nick + String.valueOf(level) + country + GameData.codeX;
            parameters.put("checksum", md5(hashString));
        } catch (NoSuchAlgorithmException e) {
            callback.failedToInitializeMD5();
        }

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setUrl("http://sheepsgohome-com.svethostingu-tmp.cz/leaderboard/update.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        callback.connecting();

        Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
            public String status;

            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (isTerminated())
                    return;

                status = httpResponse.getResultAsString();

                //parse message
                Json json = new Json();
                String response = json.fromJson(String.class, status);

                if (response.equals(RESPONSE_DB_CONNECTION_ERROR)) {
                    callback.connectionToDatabaseFailed();
                } else if (response.equals(RESPONSE_INVALID_DATA)) {
                    callback.invalidData();
                } else if (response.equals(RESPONSE_NICK_ALREADY_IN_USE)) {
                    callback.nickAlreadyInUse();
                } else if (response.equals(RESPONSE_SUCCESS)) {
                    callback.success();
                } else if (response.equals(RESPONSE_FAILURE)) {
                    callback.failure();
                } else {
                    callback.failure();
                }


            }

            public void failed(Throwable t) {
                if (isTerminated())
                    return;

                callback.connectionFailed();
            }

            @Override
            public void cancelled() {
                if (isTerminated())
                    return;

                callback.connectionCanceled();
            }
        });
    }

    public String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if (input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while (result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }

    public synchronized boolean isTerminated() {
        return terminated;
    }

    public synchronized void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }
}

