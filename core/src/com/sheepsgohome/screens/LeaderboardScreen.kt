package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.dialogs.LeaderboardResultDialog
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.gdx.onClick
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.leaderboard
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton

class LeaderboardScreen : MenuScreen(), GoogleConnectionCallback {

    private val MAX_PLAYER_NAME_LENGTH = 16

    private val buttonBack = SmallSheepButton(Loc.back)
    private val title = ScreenTitle(Loc.leaderboard)

    private val contentTable = Table()

    private var messageDialog: MessageDialog? = null

    init {
        //click listeners
        buttonBack.onClick {
            switchToMainMenuScreen()
        }

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

        GameData.leaderboard?.let {
            it.registerConnectionCallback(this)
            it.connect()
        }

    }

    override fun onConnecting() {
        messageDialog = MessageDialog(Loc.connectingToGoogle).apply {
            fixedHeight = 50f
        }

        messageDialog?.show(stage)
    }

    override fun onConnected() {
        hideMessageDialog()

        messageDialog = MessageDialog(Loc.downloadingData).apply {
            fixedHeight = 55f
            addCancelButtonWithAction { leaderboard?.cancelPendingResult() }
        }

        messageDialog?.show(stage)

        leaderboard?.fetchLeaderboardData { result ->
            showLeaderboardResult(result)
        }
    }

    override fun onConnectionFailure() {
        hideMessageDialog()
        showFailureOkDialog(Loc.connectionFailed, 60f)
    }

    override fun onOperationAborted() {
        hideMessageDialog()
    }

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
            contentTable.add(getTableHeaderLabel(Loc.player)).height(headingHeight)
            contentTable.add(getTableHeaderLabel(Loc.levelHeading)).height(headingHeight).row()

            for (row in result.leaderboardRows) {
                contentTable.add(getTableRowLabel(row.rank.toString()))
                contentTable.add(getTableRowLabel(row.nick.take(MAX_PLAYER_NAME_LENGTH)))
                contentTable.add(getTableRowLabel(row.level.toString())).row()
            }

            val emptyLabel = Label("", skin)
            contentTable.add(emptyLabel).size(20f, 1f)
            contentTable.add(emptyLabel).size(110f, 1f)
            contentTable.add(emptyLabel).size(50f, 1f).row()

            //restore Level if it is higher
            result.myResult?.let {
                with(it) {
                    if (level > GameData.LEVEL) {
                        GameData.LEVEL = level.toInt()
                        GameData.savePreferences()
                    }

                    LeaderboardResultDialog(rank).show(stage)
                }
            }

        }
    }

    private fun getTableHeaderLabel(text: String) = Label(text, skin, "menuTitle").apply {
        setFontScale(0.25f)
    }

    private fun getTableRowLabel(text: String) = Label(text, skin).apply {
        setFontScale(0.32f)
    }

    override fun hide() {
        super.hide()
        GameData.leaderboard?.let {
            it.unregisterConnectionCallback(this)
        }
    }
}
