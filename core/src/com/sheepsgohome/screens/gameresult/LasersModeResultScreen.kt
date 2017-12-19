package com.sheepsgohome.screens.gameresult

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sheepsgohome.enums.GameState
import com.sheepsgohome.enums.GameState.*
import com.sheepsgohome.gdx.onClick
import com.sheepsgohome.gdx.screens.switchToGameplayLasersModeScreen
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.SOUND_VOLUME
import com.sheepsgohome.shared.GameSounds.soundSheepSuccess
import com.sheepsgohome.shared.GameSounds.soundWolfFailure
import com.sheepsgohome.ui.MediumSheepButton
import com.sheepsgohome.ui.menuTitleLabel

class LasersModeResultScreen(gameState: GameState) : MenuScreen() {

    private var buttonRetry: MediumSheepButton? = null
    private var buttonNext: MediumSheepButton? = null

    private val buttonQuit = MediumSheepButton(Loc.quit).apply {
        onClick { switchToMainMenuScreen() }
    }

    private val title: Label

    private val sheepTexture = Texture("sheep_success.png").apply {
        setFilter(Linear, Linear)
    }

    private val wolfTexture = when (gameState) {
        GAME_OVER_BY_ALPHA_WOLF -> Texture("wolf_alpha_fail.png")
        GAME_OVER_BY_HUNGRY_WOLF -> Texture("wolf_hungry_fail.png")
        else -> Texture("wolf_fail.png")
    }.apply { setFilter(Linear, Linear) }

    private val sheepImage by lazy { Image(sheepTexture) }
    private val wolfImage by lazy { Image(wolfTexture) }

    init {
        when(gameState) {
            NEXT_LEVEL -> {
                buttonNext = MediumSheepButton(Loc.nextLevel).apply {
                    onClick { switchToGameplayLasersModeScreen() }
                }
            }
            else -> {
                buttonRetry = MediumSheepButton(Loc.retry).apply {
                    onClick { switchToGameplayLasersModeScreen() }
                }
            }
        }

        title = when (gameState) {
            NEXT_LEVEL -> menuTitleLabel(Loc.homeSweetHome)
            GAME_OVER_BY_LASER_BURN -> menuTitleLabel(Loc.sheepBurnedToAshes)
            else -> menuTitleLabel(Loc.sheepHasBeenCaught)
        }

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)

        //table
        table.add(title)
                .top()
                .height(30f)
                .colspan(2)
                .row()

        val sound: Sound

        when(gameState) {
            NEXT_LEVEL -> {
                sound = soundSheepSuccess

                //display spared wolves info
//                table.add(createBadgesTable(GameData.LEVEL))
//                    .expandX()
//                    .colspan(2)
//                    .row()

                GameData.levelUpLasersMode()

                val mult = 0.30f

                table.add(sheepImage)
                    .size(sheepImage.width * mult, sheepImage.height * mult)
                    .colspan(2)
                    .expand()
                    .row()

                buttonNext?.addTo(table)
            }
            GAME_OVER_BY_LASER_BURN -> {
                //todo
                sound = soundWolfFailure
                buttonRetry?.addTo(table)
            }
            else -> {
                sound = soundWolfFailure

                val mult = CAMERA_WIDTH * 2 / wolfImage.prefWidth * 0.95f

                table.add(wolfImage)
                    .size(wolfImage.width * mult, wolfImage.height * mult)
                    .colspan(2)
                    .expand()
                    .row()

                buttonRetry?.addTo(table)
            }
        }

        buttonQuit.addTo(table)

        if (SOUND_ENABLED) {
            sound.play(SOUND_VOLUME)
        }
    }

    override fun dispose() {
        super.dispose()
        sheepTexture.dispose()
        wolfTexture.dispose()
    }
}


