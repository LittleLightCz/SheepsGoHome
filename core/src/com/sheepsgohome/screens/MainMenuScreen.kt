package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.gdx.screens.switchToGameplayClassicModeScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.MUSIC_ENABLED
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameMusic.ambient
import com.sheepsgohome.shared.GameMusic.sheepsTheme
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.SheepButton

class MainMenuScreen : Screen {

    private val multiplier = 2f

    private val stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))
    private val table = Table()

    private val buttonPlay = SheepButton(loc.get("play"))
    private val buttonLeaderboard = SheepButton(loc.get("leaderboard"))
    private val buttonSettings = SheepButton(loc.get("settings"))
    private val buttonExit = SheepButton(loc.get("exit"))

    private val buttonSupport = ImageButton(skin, "support")
    private val musicCheckBox = CheckBox("", skin).apply {
        isChecked = MUSIC_ENABLED
    }

    private val title = Label(loc.get("sheeps.go.home"), skin, "menuTitle")
    private val version = Label(GameData.VERSION_STRING, skin)

    private val backgroundTexture = Texture("menu_background.png")
    private val backgroundImage = Image(backgroundTexture)

    init {

    }

    override fun show() {





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

        buttonSupport.addListener(clicked {
            switchScreen(SupportScreen())
        })

        //click listeners
        buttonPlay.onClick {
            if (sheepsTheme.isPlaying) {
                sheepsTheme.pause()
            }

            switchToGameplayClassicModeScreen()
        }

        buttonLeaderboard.onClick {
            switchScreen(LeaderboardScreen())
        }

        buttonSettings.onClick {
            switchScreen(SettingsScreen())
        }

        buttonExit.onClick { Gdx.app.exit() }

        //table
        table.setFillParent(true)

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)
        table.add<Label>(title).colspan(2).height(25f).padTop(5f).row()

        table.add<Label>(version).expandX().colspan(2).top().center().row()

        val buttonsTable = Table().apply {
            buttonPlay.addTo(this).row()
            buttonLeaderboard.addTo(this).row()
            buttonSettings.addTo(this).row()
            buttonExit.addTo(this).row()
        }

        table.add(buttonsTable).expandY().colspan(2).row()

        val donateSizeScale = 1.65f

        val checkBoxSize = BUTTON_WIDTH / (donateSizeScale * 2)
        table.add<CheckBox>(musicCheckBox).left().bottom().size(checkBoxSize, checkBoxSize).padLeft(2f).padBottom(2f)

        version.setFontScale(0.35f)
        table.add<ImageButton>(buttonSupport).size(checkBoxSize, checkBoxSize)
                .bottom().right().padRight(1f).padBottom(2f).row()


        stage.addActor(backgroundImage)
        stage.addActor(table)

        Gdx.input.inputProcessor = stage

        backgroundImage.width = CAMERA_WIDTH * multiplier
        backgroundImage.height = CAMERA_HEIGHT * multiplier
        backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

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
    }

    override fun dispose() {
        stage.dispose()
        backgroundTexture.dispose()
    }

    companion object {
        private val BUTTON_WIDTH = 100f
    }
}
