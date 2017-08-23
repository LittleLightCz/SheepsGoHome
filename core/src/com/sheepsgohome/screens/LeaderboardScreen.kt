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
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.leaderboard
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class LeaderboardScreen : MenuScreen(), GoogleConnectionCallback {

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
            if (!leaderboard.isConnected) {
                leaderboard.registerConnectionCallback(this)
                leaderboard.connect()
            } else {
                onConnected()
            }
        }

    }

    override fun onConnected() {
        messageDialog = MessageDialog(loc.get("connecting")).apply {
            fixedHeight = 60f
            addCancelButtonWithAction { leaderboard?.cancelPendingResult() }
        }

        messageDialog?.show(stage)

        leaderboard?.fetchLeaderboardData { result ->
            showLeaderboardResult(result)
        }
    }

    override fun onConnectionFailure() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("connection.failed"), 60f)
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


    private fun showLeaderboardResult(result: LeaderBoardResult) {
        Gdx.app.postRunnable {
            hideMessageDialog()

            val headingHeight = 20f

            contentTable.add(getTableHeaderLabel("#")).height(headingHeight)
            contentTable.add(getTableHeaderLabel(loc.get("player"))).height(headingHeight)
            contentTable.add(getTableHeaderLabel(loc.get("level.heading"))).height(headingHeight).row()

            for (row in result.leaderboardRows) {
                contentTable.add(getTableRowLabel(row.rank.toString()))
                contentTable.add(getTableRowLabel(row.nick))
                contentTable.add(getTableRowLabel(row.level.toString())).row()
            }

            val emptyLabel = Label("", skin)
            contentTable.add(emptyLabel).size(20f, 1f)
            contentTable.add(emptyLabel).size(110f, 1f)
            contentTable.add(emptyLabel).size(50f, 1f).row()

            //restore Level if it is higher
            if (result.myLevel > GameData.LEVEL) {
                GameData.LEVEL = result.myLevel
                GameData.savePreferences()
            }

            LeaderboardResultDialog(result.myRank).apply {
                fixedHeight = 70f
            }.show(stage)

        }
    }

    private fun getTableHeaderLabel(text: String) = Label(text, skin, "menuTitle").apply {
        setFontScale(0.25f)
    }

    private fun getTableRowLabel(text: String) = Label(text, skin).apply {
        setFontScale(0.32f)
    }

}
