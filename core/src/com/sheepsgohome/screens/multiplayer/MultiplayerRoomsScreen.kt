package com.sheepsgohome.screens.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.multiplayer.MultiplayerRoom
import com.sheepsgohome.screens.MenuScreen
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class MultiplayerRoomsScreen : MenuScreen() {

    private val SCREEN_TITLE_ROW_HEIGHT = 20f
    private val TABLE_ROW_HEIGHT = 15f

    private val backButton = SmallSheepButton(loc.get("back"))

    private val createButton = SmallSheepButton(loc.get("create")).apply {
        buttonWidth = 55f
    }

    private val refreshButton = SmallSheepButton(loc.get("refresh")).apply {
        buttonWidth = 65f
    }

    private val title = ScreenTitle(loc.get("online.rooms"))

    private val hashLabel = HeaderLabel("#")
    private val hashLabelPadding = 2f

    private val roomsTable = Table()

    init {
        backButton.onClick {
            switchToMainMenuScreen()
        }

        createButton.onClick {
            //todo
        }

        refreshButton.onClick {
            refreshRoomsData()
        }

        table.add(title)
                .height(SCREEN_TITLE_ROW_HEIGHT)
                .colspan(3)
                .top()
                .row()

        table.add(HeaderLabel(loc.get("room")))
                .height(TABLE_ROW_HEIGHT)
                .expandX()

        table.add(hashLabel)
                .height(TABLE_ROW_HEIGHT)
                .padLeft(hashLabelPadding)
                .padRight(hashLabelPadding)
                .center()

        //Empty column
        table.add()
                .width(SmallSheepButton.BUTTON_WIDTH)
                .row()

        val scrollPane = ScrollPane(roomsTable)
        scrollPane.scrollTo(0f, 0f, 0f, 0f, false, false)

        table.add(scrollPane)
                .colspan(3)
                .size(CAMERA_WIDTH * 2, 256f)
                .expand()
                .top()
                .row()

        //Bottom buttons
        val bottomButtonsTable = Table().apply {
            refreshButton.addTo(this)

            createButton.addTo(this)
                    .padLeft(1f)

            backButton.addTo(this)
                    .padLeft(1f)
        }

        table.add(bottomButtonsTable)
                .colspan(3)
                .padBottom(2f)
                .row()

        Gdx.input.inputProcessor = stage

//        table.debug()

        refreshRoomsData()

        //todo remove
        reloadRoomsTable(listOf(
                MultiplayerRoom("SomeRoomName", 3)
        ))
    }

    private fun refreshRoomsData() {

    }

    private fun reloadRoomsTable(rooms: List<MultiplayerRoom>) {
        with(roomsTable) {
            clear()

            rooms.forEach {
                add(RowLabel(it.name))
                        .expandX()

                val playersLabel = RowLabel(it.players.toString()).apply {
                    setAlignment(Align.center)
                }

                add(playersLabel)
                        .width(hashLabel.prefWidth)
                        .padLeft(hashLabelPadding)
                        .padRight(hashLabelPadding)

                val joinButton = SmallSheepButton(loc.get("join")).apply {
                    onClick {
                        joinRoom(it)
                    }
                }

                joinButton.addTo(this).row()
            }
        }
    }

    private fun joinRoom(room: MultiplayerRoom) {

    }

}

private class HeaderLabel(text: String) : Label(text, GameSkins.skin, "menuTitle") {
    init {
        setFontScale(0.25f)
    }
}

private class RowLabel(text: String) : Label(text, GameSkins.skin) {
    init {
        setFontScale(0.32f)
    }
}
