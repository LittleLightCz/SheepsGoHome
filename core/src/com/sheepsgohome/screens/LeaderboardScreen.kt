package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.dialogs.LeaderboardResultDialog
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.leaderboard.LeaderBoardCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class LeaderboardScreen : MenuScreen(), LeaderBoardCallback, GoogleConnectionCallback {

//    private val leaderBoard = LeaderBoard.instance

    private val buttonBack = SmallSheepButton(loc.get("back"))
    private val title = Label(loc.get("leaderboard"), skin, "menuTitle")

    private val contentTable = Table()

    private var messageDialog: MessageDialog? = null

    init {
        //click listeners
        buttonBack.onClick {
            switchToMainMenuScreen()
        }

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add(title).top().row()

        val scrollPane = ScrollPane(contentTable)
        scrollPane.scrollTo(0f, 0f, 0f, 0f, false, false)

        table.add(scrollPane)
                .size(CAMERA_WIDTH * 2, 256f)
                .expand()
                .top()
                .row()

        buttonBack.addTo(table)
                .bottom()
                .center()
                .row()

        Gdx.input.inputProcessor = stage

        GameData.leaderboard?.let { leaderboard ->
            leaderboard.registerConnectionCallback(this)

            if (!leaderboard.isConnected) {
                leaderboard.connect()
            }
        }




        //update leadeboard first
//        if (GameData.PLAYER_NAME == "") {
//            unregisteredUser()
//        } else {
//            leaderBoard.register(
//                    GameData.androidFunctions.deviceId,
//                    GameData.PLAYER_NAME,
//                    GameData.LEVEL,
//                    GameData.androidFunctions.countryCode,
//                    this)
//        }
    }


    override fun onConnected() {

    }

    override fun onConnectionFailure() {

    }


    /**
     * LeaderBoard callback
     */
    private fun hideMessageDialog() {
        messageDialog?.hide()
    }

    private fun showFailureOkDialog(message: String, dialogHeight: Float) {
        OkDialog(message).apply {
            fixedHeight = dialogHeight
            onConfirm = { switchToMainMenuScreen() }
        }.show(stage)
    }

    override fun connecting() {
        messageDialog = MessageDialog(loc.get("connecting")).apply {
            fixedHeight = 60f
//            addCancelButtonWithAction { leaderBoard.isTerminated = true }
        }

        messageDialog?.show(stage)
    }

    override fun connectionToDatabaseFailed() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("connection.to.database.failed"), 60f)
    }

    override fun invalidData() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("invalid.data"), 80f)
    }

    override fun nickAlreadyInUse() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("player.name.already.in.use"), 90f)
    }

    override fun success() {
        hideMessageDialog()

        //fetch leaderboard
//        if (!leaderBoard.isTerminated) {
//            leaderBoard.fetchLeaderboard(GameData.androidFunctions.deviceId, this)
//        }
    }

    override fun failure() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("unknown.failure"), 60f)
    }

    override fun failedToInitializeMD5() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("md5.init.failed"), 95f)
    }

    override fun connectionFailed() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("connection.failed"), 60f)
    }

    override fun connectionCanceled() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("connection.canceled"), 85f)
    }

    override fun unregisteredUser() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("unregistered.player"), 80f)
    }

    override fun leaderboardResult(result: LeaderBoardResult) {
        Gdx.app.postRunnable {
            hideMessageDialog()

            val headingHeight = 20f

            contentTable.add(getTableHeaderLabel("#")).height(headingHeight)
            contentTable.add(getTableHeaderLabel(loc.get("player"))).height(headingHeight)
            contentTable.add(getTableHeaderLabel(loc.get("level.heading"))).height(headingHeight).row()

            for (row in result.leaderboard) {
                contentTable.add(getTableRowLabel(row.rank.toString()))
                contentTable.add(getTableRowLabel(row.nick))
                contentTable.add(getTableRowLabel(row.level.toString())).row()
            }

            val emptyLabel = Label("", skin)
            contentTable.add(emptyLabel).size(20f, 1f)
            contentTable.add(emptyLabel).size(110f, 1f)
            contentTable.add(emptyLabel).size(50f, 1f).row()

            //restore Level if it is higher
            result.mypos?.level?.let {
                if (it > GameData.LEVEL) {
                    GameData.LEVEL = it
                    GameData.savePreferences()
                }
            }

            result.mypos?.rank?.let {
                LeaderboardResultDialog(it).apply {
                    fixedHeight = 70f
                }.show(stage)
            }

        }
    }

    private fun getTableHeaderLabel(text: String) = Label(text, skin, "menuTitle").apply {
        setFontScale(0.25f)
    }

    private fun getTableRowLabel(text: String) = Label(text, skin).apply {
        setFontScale(0.32f)
    }

}
