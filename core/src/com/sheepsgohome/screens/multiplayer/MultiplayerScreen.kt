package com.sheepsgohome.screens.multiplayer

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.gdx.onClick
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton

class MultiplayerScreen : MenuScreen(), GoogleConnectionCallback {

    private val SCREEN_TITLE_ROW_HEIGHT = 20f

    private val backButton = SmallSheepButton(Loc.back)

    private val createButton = BigSheepButton(Loc.create)
    private val searchButton = BigSheepButton(Loc.search)

    private val title = ScreenTitle(Loc.multiplayer)

    private var messageDialog: MessageDialog? = null

    init {
        createButton.onClick {
            messageDialog = MessageDialog(Loc.creatingMultiplayerGame).apply {
                fixedHeight = 50f
            }

            messageDialog?.show(stage)

            GameData.multiplayer?.create()
        }

        searchButton.onClick {
            messageDialog = MessageDialog(Loc.searchingForMultiplayerGames).apply {
                fixedHeight = 50f
            }

            messageDialog?.show(stage)

            GameData.multiplayer?.search()
        }

        backButton.onClick {
            GameData.multiplayer?.unregisterConnectionCallback(this)
            switchToMainMenuScreen()
        }

        table.add(title)
                .height(SCREEN_TITLE_ROW_HEIGHT)
                .top()
                .row()

        val menuButtonsTable = Table().apply {
            createButton.addTo(this).row()
            searchButton.addTo(this).row()
        }

        //Bottom buttons
        val bottomButtonsTable = Table().apply {
            backButton.addTo(this)
        }

        table.add(menuButtonsTable)
                .expand()
                .row()

        table.add(bottomButtonsTable)
                .padBottom(2f)
                .row()

        GameData.multiplayer?.let {
            it.registerConnectionCallback(this)
            it.connect()
        }
    }

    override fun onConnected() {
        hideMessageDialog()
    }

    override fun onConnecting() {
        messageDialog = MessageDialog(Loc.connectingToGoogle).apply {
            fixedHeight = 50f
        }

        messageDialog?.show(stage)
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

}