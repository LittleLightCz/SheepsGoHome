package com.sheepsgohome.screens.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.google.leaderboard.GoogleConnectionCallback
import com.sheepsgohome.screens.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class MultiplayerScreen : MenuScreen(), GoogleConnectionCallback {

    private val SCREEN_TITLE_ROW_HEIGHT = 20f

    private val backButton = SmallSheepButton(loc.get("back"))

    private val createButton = BigSheepButton(loc.get("create"))
    private val searchButton = BigSheepButton(loc.get("search"))

    private val title = ScreenTitle(loc.get("multiplayer"))

    private var messageDialog: MessageDialog? = null

    init {
        backButton.onClick {
            GameData.multiplayer?.unregisterConnectionCallback(this)
            switchToMainMenuScreen()
        }

        createButton.onClick {
            GameData.multiplayer?.create()
        }

        searchButton.onClick {

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

        Gdx.input.inputProcessor = stage

        GameData.multiplayer?.let {
            it.registerConnectionCallback(this)
            it.connect()
        }
    }

    override fun onConnected() {
        hideMessageDialog()
    }

    override fun onConnecting() {
        messageDialog = MessageDialog(loc.get("connecting.to.google")).apply {
            fixedHeight = 50f
        }

        messageDialog?.show(stage)
    }

    override fun onConnectionFailure() {
        hideMessageDialog()
        showFailureOkDialog(loc.get("connection.failed"), 60f)
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