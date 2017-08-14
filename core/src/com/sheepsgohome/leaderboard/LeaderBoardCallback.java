package com.sheepsgohome.leaderboard;

public interface LeaderBoardCallback {

    void connecting();

    void connectionToDatabaseFailed();

    void invalidData();

    void nickAlreadyInUse();

    void success();

    void failure();

    void failedToInitializeMD5();

    void connectionFailed();

    void connectionCanceled();

    void unregisteredUser();

    void leaderboardResult(LeaderBoardResult result);
}
