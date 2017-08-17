package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.dialogs.LeaderboardResultDialog
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.leaderboard.LeaderBoard
import com.sheepsgohome.leaderboard.LeaderBoardCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin

class LeaderboardScreen : Screen, LeaderBoardCallback, MessageDialog.CancelAction {

    companion object {
        private val BUTTON_SMALL_WIDTH = 50f
    }

    private val leaderBoard = LeaderBoard.instance

    private val multiplier = 2f
    private val stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

    private val table = Table()
    private val buttonBack = TextButton(loc.get("back"), skin)
    private val title = Label(loc.get("leaderboard"), skin, "menuTitle")
    private var messageDialog: MessageDialog? = null

    private val texture = Texture("menu_background.png")
    private val bgImage = Image(texture)

    private val contentTable = Table()

    override fun show() {
        //click listeners
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = GameScreens.mainMenuScreen
            }
        })

        //table
        table.setFillParent(true)

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add<Label>(title).top().row()

        val scrollPane = ScrollPane(contentTable)
        scrollPane.scrollTo(0f, 0f, 0f, 0f, false, false)

        table.add(scrollPane).size(CAMERA_WIDTH * 2, 256f).expand().top().row()

        table.add<TextButton>(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().center().padBottom(2f).row()

        stage.addActor(bgImage)
        stage.addActor(table)

        Gdx.input.inputProcessor = stage

        bgImage.width = CAMERA_WIDTH * multiplier
        bgImage.height = CAMERA_HEIGHT * multiplier
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        //update leadeboard first
        if (GameData.PLAYER_NAME == "") {
            unregisteredUser()
        } else {
            leaderBoard.register(
                    GameData.functions.deviceId,
                    GameData.PLAYER_NAME,
                    GameData.LEVEL,
                    GameData.functions.countryCode,
                    this)
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        stage.dispose()
        texture.dispose()
    }

    /**
     * LeaderBoard callback
     */
    private fun hideMessageDialog() {
        messageDialog?.hide()
    }

    override fun connecting() {
        messageDialog = MessageDialog(loc.get("connecting"), skin, "dialog").apply {
            prefHeight = 60f
        }

        messageDialog?.addCancelButton(this)
        messageDialog?.show(stage)
    }

    override fun connectionToDatabaseFailed() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.to.database.failed"), skin, "dialog")
        dialog.prefHeight = 60f
        dialog.show(stage)
    }

    override fun invalidData() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("invalid.data"), skin, "dialog")
        dialog.prefHeight = 80f
        dialog.show(stage)
    }

    override fun nickAlreadyInUse() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("player.name.already.in.use"), skin, "dialog")
        dialog.prefHeight = 90f
        dialog.setRedirectScreen(SettingsPlayerScreen())
        dialog.show(stage)
    }


    override fun success() {
        hideMessageDialog()

        //fetch leaderboard
        if (!leaderBoard.isTerminated)
            leaderBoard.fetchLeaderboard(GameData.functions.deviceId, this)
    }

    override fun failure() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("unknown.failure"), skin, "dialog")
        dialog.prefHeight = 60f
        dialog.show(stage)
    }

    override fun failedToInitializeMD5() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("md5.init.failed"), skin, "dialog")
        dialog.prefHeight = 95f
        dialog.show(stage)
    }

    override fun connectionFailed() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.failed"), skin, "dialog")
        dialog.prefHeight = 60f
        dialog.show(stage)
    }

    override fun connectionCanceled() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("connection.canceled"), skin, "dialog")
        dialog.prefHeight = 85f
        dialog.show(stage)
    }

    override fun unregisteredUser() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("unregistered.player"), skin, "dialog")
        dialog.prefHeight = 80f
        dialog.setRedirectScreen(SettingsPlayerScreen())
        dialog.show(stage)
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

            val dialog = LeaderboardResultDialog(result.mypos?.rank ?: 0, skin, "dialog")
            dialog.prefHeight = 70f
            dialog.show(stage)
        }
    }

    private fun getTableHeaderLabel(text: String): Label {
        val l = Label(text, skin, "menuTitle")
        l.setFontScale(0.25f)
        return l
    }

    private fun getTableRowLabel(text: String): Label {
        val l = Label(text, skin)
        l.setFontScale(0.32f)
        return l
    }

    override fun CancelPressed() {
        leaderBoard.isTerminated = true
    }

}
