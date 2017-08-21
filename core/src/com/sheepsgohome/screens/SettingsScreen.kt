package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin

class SettingsScreen : Screen {
    private lateinit var stage: Stage
    private lateinit var table: Table

    private lateinit var buttonPlayer: TextButton
    private lateinit var buttonControls: TextButton
    private lateinit var buttonSound: TextButton
    private lateinit var buttonBack: TextButton
    private lateinit var title: Label

    private lateinit var texture: Texture
    private lateinit var bgImage: Image

    override fun show() {
        buttonPlayer = TextButton(loc.get("player"), skin)
        buttonControls = TextButton(loc.get("controls"), skin)
        buttonSound = TextButton(loc.get("sound"), skin)
        buttonBack = TextButton(loc.get("back"), skin)
        title = Label(loc.get("settings"), skin, "menuTitle")

        texture = Texture("menu_background.png")
        bgImage = Image(texture)


        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        table = Table()

        //click listeners
        buttonPlayer.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsPlayerScreen()
            }
        })

        buttonControls.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsControlsScreen()
            }
        })

        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsSoundScreen()
            }
        })


        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                switchToMainMenuScreen()
            }
        })

        //table
        table.setFillParent(true)

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add<Label>(title).top().row()

        val buttonsTable = Table()

        buttonsTable.add<TextButton>(buttonPlayer).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonControls).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonSound).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonBack).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()

        table.add(buttonsTable).expandY().row()
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

    companion object {
        private val BUTTON_WIDTH = 100f
        private val BUTTON_SMALL_WIDTH = 50f
    }
}
