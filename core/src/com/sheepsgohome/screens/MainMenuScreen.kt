package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.listeners.changed
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchScreen
import com.sheepsgohome.gdx.screens.switchToGameplayClassicModeScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.MUSIC_ENABLED
import com.sheepsgohome.shared.GameMusic.ambient
import com.sheepsgohome.shared.GameMusic.sheepsTheme
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class MainMenuScreen : MenuScreen() {

    private val checkBoxSizeScale = 1.65f
    private val checkBoxSize = BigSheepButton.BUTTON_WIDTH / (checkBoxSizeScale * 2)

    private val multiplier = 2f

    private val buttonPlay = BigSheepButton(Loc.play)
    private val buttonMultiplayer = BigSheepButton(Loc.multiplayer)
    private val buttonLeaderboard = BigSheepButton(Loc.leaderboard)
    private val buttonSettings = BigSheepButton(Loc.settings)

    private val buttonExit = SmallSheepButton(Loc.exit).apply {
        buttonWidth += 20
        buttonHeight = checkBoxSize
    }

    private val buttonSupport = ImageButton(skin, "support")
    private val musicCheckBox = CheckBox("", skin).apply {
        isChecked = MUSIC_ENABLED
    }

    private val titleLabel = Label(Loc.sheepsGoHome, skin, "menuTitle")
    private val versionLabel = Label(GameData.VERSION_STRING, skin).apply {
        setFontScale(0.35f)
    }

    init {
        val tableColspan = 3

        musicCheckBox.addListener(changed { _, checkBox ->
            if (checkBox is CheckBox) {
                if (checkBox.isChecked) {
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
        })

        buttonSupport.addListener(clicked {
            switchScreen(SupportScreen())
        })

        buttonPlay.onClick {
           switchScreen(ChooseModeScreen())
        }

//        buttonMultiplayer.onClick {
//            switchScreen(MultiplayerScreen())
//        }

        buttonLeaderboard.onClick {
            switchScreen(LeaderboardScreen())
        }

        buttonSettings.onClick {
            switchScreen(SettingsScreen())
        }

        buttonExit.onClick { Gdx.app.exit() }

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / titleLabel.prefWidth
        titleLabel.setFontScale(fontScale)

        table.add(titleLabel)
                .colspan(tableColspan)
                .height(20f)
                .row()

        table.add(versionLabel)
                .colspan(tableColspan)
                .top()
                .center()
                .row()

        val verticalButtonsTable = Table().apply {
            buttonPlay.addTo(this).row()
//            buttonMultiplayer.addTo(this).row()

            GameData.leaderboard?.let {
                buttonLeaderboard.addTo(this).row()
            }

            buttonSettings.addTo(this).row()
        }

        table.add(verticalButtonsTable)
                .expandY()
                .expandX()
                .colspan(tableColspan)
                .row()

        // bottom buttons
        table.add(musicCheckBox)
                .left()
                .bottom()
                .size(checkBoxSize, checkBoxSize)
                .padLeft(2f)
                .padBottom(2f)

        buttonExit.addTo(table)
                .padBottom(2f)

        table.add(buttonSupport)
                .size(checkBoxSize, checkBoxSize)
                .bottom()
                .right()
                .padRight(1f)
                .padBottom(2f)
                .row()

        if (ambient.isPlaying) {
            ambient.pause()
        }

        if (MUSIC_ENABLED && !sheepsTheme.isPlaying) {
            sheepsTheme.play()
        }
    }
}
