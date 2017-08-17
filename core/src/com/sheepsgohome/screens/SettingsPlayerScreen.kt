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
import com.sheepsgohome.GameSkins.skin
import com.sheepsgohome.dialogs.MessageDialog
import com.sheepsgohome.dialogs.OkDialog
import com.sheepsgohome.leaderboard.LeaderBoard
import com.sheepsgohome.leaderboard.LeaderBoardCallback
import com.sheepsgohome.leaderboard.LeaderBoardResult
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc

class SettingsPlayerScreen : Screen, LeaderBoardCallback {

    private val leaderBoard = LeaderBoard.instance

    private lateinit var stage: Stage
    private lateinit var table: Table

    private lateinit var buttonRegister: TextButton
    private lateinit var buttonSave: TextButton
    private lateinit var buttonBack: TextButton

    private lateinit var playerName: TextField

    private lateinit var title: Label
    private lateinit var playerNameTitle: Label

    private var messageDialog: MessageDialog? = null

    private lateinit var texture: Texture
    private lateinit var bgImage: Image

    override fun show() {
        buttonRegister = TextButton(loc.get("register"), skin)
        buttonSave = TextButton(loc.get("save"), skin)
        buttonBack = TextButton(loc.get("back"), skin)
        title = Label(loc.get("player"), skin, "menuTitle")
        playerNameTitle = Label(loc.get("player.name"), skin, "default")

        playerName = TextField("", skin)
        playerName.maxLength = 16
        playerName.setAlignment(1)
        playerName.text = GameData.PLAYER_NAME

        texture = Texture("menu_background.png")
        bgImage = Image(texture)

        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        table = Table()

        //click listeners
        buttonSave.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                GameData.PLAYER_NAME = validatePlayerName()
                GameData.savePreferences()
                (Gdx.app.applicationListener as Game).screen = SettingsScreen()
            }
        })

        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsScreen()
            }
        })


        val __this = this
        buttonRegister.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {

                val nick = validatePlayerName()

                if (nick == "") {
                    val dialog = OkDialog(loc.get("empty.player.name"), skin, "dialog")
                    dialog.prefHeight = 60f
                    dialog.show(stage)
                } else {
                    leaderBoard.register(
                            GameData.functions.deviceId,
                            nick,
                            GameData.LEVEL,
                            GameData.functions.countryCode,
                            __this)
                }
            }
        })


        //table
        table.setFillParent(true)

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add<Label>(title).top().colspan(2).row()

        val contentTable = Table()
        playerNameTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE)
        contentTable.add<Label>(playerNameTitle).expandX().row()
        contentTable.add<TextField>(playerName).size(170f, BUTTON_SMALL_WIDTH / 2).padTop(5f).padBottom(5f).row()

        val BUTTON_REGISTER_WIDTH = BUTTON_WIDTH
        contentTable.add<TextButton>(buttonRegister).size(BUTTON_REGISTER_WIDTH, BUTTON_REGISTER_WIDTH / 2).row()


        table.add(contentTable).expand().colspan(2).top().row()

        table.add<TextButton>(buttonSave).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().right()
        table.add<TextButton>(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().left().row()

        stage.addActor(bgImage)
        stage.addActor(table)

        Gdx.input.inputProcessor = stage

        bgImage.width = CAMERA_WIDTH * multiplier
        bgImage.height = CAMERA_HEIGHT * multiplier
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
    }

    private fun validatePlayerName(): String {
        val nick = playerName.text.replace("[^\\w_\\-]".toRegex(), "")
        playerName.text = nick
        return nick
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
        messageDialog = MessageDialog(loc.get("connecting"), skin, "dialog")
        messageDialog?.prefHeight = 40f
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
        dialog.show(stage)
    }


    override fun success() {
        hideMessageDialog()

        val dialog = OkDialog(loc.get("player.name.registration.success"), skin, "dialog")
        dialog.prefHeight = 70f
        dialog.show(stage)

        GameData.PLAYER_NAME = playerName.text
        GameData.savePreferences()

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

    }

    override fun leaderboardResult(result: LeaderBoardResult) {

    }

    companion object {
        private val BUTTON_WIDTH = 100f
        private val BUTTON_SMALL_WIDTH = 50f
    }
}
