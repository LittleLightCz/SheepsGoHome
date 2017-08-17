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
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.Loc
import com.sheepsgohome.shared.GameData.VIRTUAL_JOYSTICK

class SettingsControlsScreen : Screen {
    private lateinit var stage: Stage
    private lateinit var table: Table

    private lateinit var buttonSave: TextButton
    private lateinit var buttonBack: TextButton

    private lateinit var title: Label
    private lateinit var virtualJoystickTitle: Label

    private lateinit var texture: Texture
    private lateinit var bgImage: Image

    override fun show() {
        buttonSave = TextButton(Loc.get("save"), skin)
        buttonBack = TextButton(Loc.get("back"), skin)
        title = Label(Loc.get("controls"), skin, "menuTitle")
        virtualJoystickTitle = Label(Loc.get("virtual.joystick"), skin, "default")

        texture = Texture("menu_background.png")
        bgImage = Image(texture)

        val virtualJoystickSelectBox = SelectBox<String>(skin)
        virtualJoystickSelectBox.setItems(Loc.get("none"), Loc.get("left"), Loc.get("right"))
        virtualJoystickSelectBox.selectedIndex = VIRTUAL_JOYSTICK

        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        table = Table()

        //click listeners
        buttonSave.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                VIRTUAL_JOYSTICK = virtualJoystickSelectBox.selectedIndex
                GameData.SavePreferences()
                (Gdx.app.applicationListener as Game).screen = SettingsScreen()
            }
        })

        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsScreen()
            }
        })

        //table
        table.setFillParent(true)

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add<Label>(title).top().colspan(2).row()

        val contentTable = Table()
        virtualJoystickTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE)
        contentTable.add<Label>(virtualJoystickTitle).expandX().padRight(2f)
        contentTable.add(virtualJoystickSelectBox).expandX().width(50f).height(20f).row()

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

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        stage.dispose()
        texture.dispose()
    }

    companion object {
        private val BUTTON_WIDTH = 100f
        private val BUTTON_SMALL_WIDTH = 50f
    }
}