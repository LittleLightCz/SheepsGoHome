package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.gdx.clicked
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.MUSIC_ENABLED
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameMusic.ambient
import com.sheepsgohome.shared.GameMusic.sheepsTheme
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin

class MainMenuScreen : Screen {
    private lateinit var stage: Stage
    private lateinit var table: Table

    private lateinit var buttonPlay: TextButton
    private lateinit var buttonLeaderboard: TextButton
    private lateinit var buttonSettings: TextButton
    private lateinit var buttonExit: TextButton
    private lateinit var buttonSupport: ImageButton

    private lateinit var title: Label
    private lateinit var version: Label

    private lateinit var musicCheckBox: CheckBox

    private lateinit var texture: Texture
    private lateinit var bgImage: Image

    override fun show() {
        buttonSupport = ImageButton(skin, "support")
        buttonPlay = TextButton(loc.get("play"), skin)

        buttonPlay.style.font.setScale(0.5f)

        buttonLeaderboard = TextButton(loc.get("leaderboard"), skin)
        buttonSettings = TextButton(loc.get("settings"), skin)
        buttonExit = TextButton(loc.get("exit"), skin)
        title = Label(loc.get("sheeps.go.home"), skin, "menuTitle")
        version = Label(GameData.VERSION_STRING, skin)
        musicCheckBox = CheckBox("", skin)
        musicCheckBox.isChecked = MUSIC_ENABLED

        texture = Texture("menu_background.png")
        bgImage = Image(texture)


        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        table = Table()
        //        table.setDebug(true);


        musicCheckBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val cbox = actor as CheckBox?
                if (cbox != null) {
                    if (cbox.isChecked) {
                        MUSIC_ENABLED = true
                        if (!sheepsTheme.isPlaying) {
                            sheepsTheme.play()
                        }
                    } else {
                        MUSIC_ENABLED = false
                        if (sheepsTheme.isPlaying) {
                            sheepsTheme.pause()
                        }
                    }
                    GameData.savePreferences()
                }
            }
        })


        //click listeners
        buttonPlay.addListener(clicked {
            if (sheepsTheme.isPlaying) {
                sheepsTheme.pause()
            }

            GameScreens.switchToGameplayClassicModeScreen()
        })

        buttonLeaderboard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = LeaderboardScreen()
            }
        })

        buttonSupport.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SupportScreen()
            }
        })

        buttonSettings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = SettingsScreen()
            }
        })

        buttonExit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })


        //table
        table.setFillParent(true)

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)
        table.add<Label>(title).colspan(2).height(25f).padTop(5f).row()

        table.add<Label>(version).expandX().colspan(2).top().center().row()

        val buttonsTable = Table()
        buttonsTable.add<TextButton>(buttonPlay).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonLeaderboard).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonSettings).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        buttonsTable.add<TextButton>(buttonExit).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()

        table.add(buttonsTable).expandY().colspan(2).row()

        val donateSizeScale = 1.65f

        val checkBoxSize = BUTTON_WIDTH / (donateSizeScale * 2)
        table.add<CheckBox>(musicCheckBox).left().bottom().size(checkBoxSize, checkBoxSize).padLeft(2f).padBottom(2f)

        version.setFontScale(0.35f)
        table.add<ImageButton>(buttonSupport).size(checkBoxSize, checkBoxSize)
                .bottom().right().padRight(1f).padBottom(2f).row()


        stage.addActor(bgImage)
        stage.addActor(table)

        Gdx.input.inputProcessor = stage

        bgImage.width = CAMERA_WIDTH * multiplier
        bgImage.height = CAMERA_HEIGHT * multiplier
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        if (ambient.isPlaying) {
            ambient.pause()
        }

        if (MUSIC_ENABLED && !sheepsTheme.isPlaying) {
            sheepsTheme.play()
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

    companion object {
        private val BUTTON_WIDTH = 100f
    }
}
