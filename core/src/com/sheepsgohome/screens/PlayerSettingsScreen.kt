package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.leaderboard.LeaderBoard
import com.sheepsgohome.leaderboard.LeaderBoardCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class PlayerSettingsScreen : MenuScreen(), LeaderBoardCallback {

    private val leaderBoard = LeaderBoard.instance

    private val buttonRegister = BigSheepButton(loc.get("register"))

    private val buttonSave = SmallSheepButton(loc.get("save"))
    private val buttonBack = SmallSheepButton(loc.get("back"))

    private val playerName = TextField("", skin).apply {
        maxLength = 16
        setAlignment(1)
        text = GameData.PLAYER_NAME
    }

    private val title = Label(loc.get("player"), skin, "menuTitle")
    private val playerNameTitle = Label(loc.get("player.name"), skin, "default")

    private var messageDialog: MessageDialog? = null

    init {
        //click listeners
        buttonSave.onClick {
            GameData.PLAYER_NAME = validatePlayerName()
            GameData.savePreferences()
            switchScreen(SettingsScreen())
        }

        buttonBack.onClick {
            switchScreen(SettingsScreen())
        }

        val callback = this
        buttonRegister.onClick {
            val nick = validatePlayerName()

//            if (nick == "") {
//                val dialog = OkDialog(loc.get("empty.player.name"))
//                dialog.fixedHeight = 60f
//                dialog.show(stage)
//            } else {
//                leaderBoard.register(
//                        GameData.androidFunctions.deviceId,
//                        nick,
//                        GameData.LEVEL,
//                        GameData.androidFunctions.countryCode,
//                        callback)
//            }
        }

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add(title)
                .top()
                .colspan(2)
                .row()

        val contentTable = Table()

        playerNameTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE)
        contentTable.add(playerNameTitle)
                .expandX()
                .row()

        contentTable.add(playerName)
                .size(170f, SmallSheepButton.BUTTON_WIDTH / 2).padTop(5f).padBottom(5f).row()

        buttonRegister.addTo(contentTable).row()

        table.add(contentTable)
                .expand()
                .colspan(2)
                .top()
                .row()

        buttonSave.addTo(table)
                .bottom()
                .right()

        buttonBack.addTo(table)
                .bottom()
                .left()
                .row()

        Gdx.input.inputProcessor = stage
    }

    private fun validatePlayerName(): String {
        val nick = playerName.text.replace("[^\\w_\\-]".toRegex(), "")
        playerName.text = nick
        return nick
    }

    /**
     * LeaderBoard callback
     */
    private fun hideMessageDialog() {
        messageDialog?.hide()
    }

    override fun connecting() {
        messageDialog = MessageDialog(loc.get("connecting")).apply {
            fixedHeight = 40f
        }

        messageDialog?.show(stage)
    }

    override fun connectionToDatabaseFailed() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.to.database.failed"))
        dialog.fixedHeight = 60f
        dialog.show(stage)
    }

    override fun invalidData() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("invalid.data"))
        dialog.fixedHeight = 80f
        dialog.show(stage)
    }

    override fun nickAlreadyInUse() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("player.name.already.in.use"))
        dialog.fixedHeight = 90f
        dialog.show(stage)
    }


    override fun success() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("player.name.registration.success"))
        dialog.fixedHeight = 70f
        dialog.show(stage)

        GameData.PLAYER_NAME = playerName.text
        GameData.savePreferences()

    }

    override fun failure() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("unknown.failure"))
        dialog.fixedHeight = 60f
        dialog.show(stage)
    }

    override fun failedToInitializeMD5() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("md5.init.failed"))
        dialog.fixedHeight = 95f
        dialog.show(stage)
    }

    override fun connectionFailed() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.failed"))
        dialog.fixedHeight = 60f
        dialog.show(stage)
    }

    override fun connectionCanceled() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.canceled"))
        dialog.fixedHeight = 85f
        dialog.show(stage)
    }

    override fun unregisteredUser() {

    }

    override fun leaderboardResult(result: LeaderBoardResult) {
    }

}
