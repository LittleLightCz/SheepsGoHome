package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.sheepsgohome.badges.Badge
import com.sheepsgohome.dialogs.NewBadgeDialog
import com.sheepsgohome.enums.GameResult
import com.sheepsgohome.enums.GameResult.*
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchToGameplayClassicModeScreen
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.SOUND_VOLUME
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.shared.GameSounds.soundNewBadge
import com.sheepsgohome.shared.GameSounds.soundSheepSuccess
import com.sheepsgohome.shared.GameSounds.soundWolfFailure
import com.sheepsgohome.ui.onClick

class ClassicModeResultScreen(gameResult: GameResult) : MenuScreen() {

    private val BADGES_COUNT = 12
    private val BADGES_PER_ROW = 6

    private val BUTTON_WIDTH = 80f

    private var buttonRetry: TextButton? = null
    private var buttonNext: TextButton? = null

    private val buttonQuit = TextButton(Loc.quit, skin).apply {
        onClick { switchToMainMenuScreen() }
    }

    private val title: Label

    private val sheepTexture = Texture("sheep_success.png").apply {
        setFilter(Linear, Linear)
    }

    private val wolfTexture = when (gameResult) {
        SHEEP_EATEN_BY_ALPHA_WOLF -> Texture("wolf_alpha_fail.png")
        SHEEP_EATEN_BY_HUNGRY_WOLF -> Texture("wolf_hungry_fail.png")
        else -> Texture("wolf_fail.png")
    }.apply { setFilter(Linear, Linear) }

    private val sheepImage by lazy { Image(sheepTexture) }
    private val wolfImage by lazy { Image(wolfTexture) }

    private val badges = (1..BADGES_COUNT).map {
        Badge(it).apply {
            image.addListener(clicked {
                NewBadgeDialog(this, isNew = false).show(stage)
            })
        }
    }

    init {
        when(gameResult) {
            SHEEP_SUCCEEDED -> {
                buttonNext = TextButton(Loc.nextLevel, skin).apply {
                    onClick { switchToGameplayClassicModeScreen() }
                }
            }
            else -> {
                buttonRetry = TextButton(Loc.retry, skin).apply {
                    onClick { switchToGameplayClassicModeScreen() }
                }
            }
        }

        title = when (gameResult) {
            SHEEP_SUCCEEDED -> Label(Loc.homeSweetHome, skin, "menuTitle")
            else -> Label(Loc.sheepHasBeenCaught, skin, "menuTitle")
        }

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)

        //table
        table.add(title)
                .top()
                .height(30f)
                .colspan(2)
                .row()

        var showNewBadgeDialog = false
        var badgeNumber = 0

        val sound: Sound

        when(gameResult) {
            SHEEP_SUCCEEDED -> {
                //has earned new badge?
                badgeNumber = getEarnedBadgeNumber(GameData.LEVEL)
                if (badgeNumber > 0) {
                    showNewBadgeDialog = true
                }

                sound = if (showNewBadgeDialog) soundNewBadge else soundSheepSuccess

                //display badges
                table.add(createBadgesTable(GameData.LEVEL))
                    .expandX()
                    .colspan(2)
                    .row()

                GameData.levelUp()

                val mult = 0.30f

                table.add(sheepImage)
                    .size(sheepImage.width * mult, sheepImage.height * mult)
                    .colspan(2)
                    .expand()
                    .row()

                table.add(buttonNext)
                    .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
            }
            else -> {
                sound = soundWolfFailure

                val mult = CAMERA_WIDTH * 2 / wolfImage.prefWidth * 0.95f

                table.add(wolfImage)
                    .size(wolfImage.width * mult, wolfImage.height * mult)
                    .colspan(2)
                    .expand()
                    .row()

                table.add(buttonRetry)
                    .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
            }
        }

        table.add(buttonQuit)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .row()


        if (showNewBadgeDialog) {
            badges.find { it.badgeNumber == badgeNumber }?.let {
                NewBadgeDialog(it).show(stage)
            }
        }

        if (SOUND_ENABLED) {
            sound.play(SOUND_VOLUME)
        }
    }

    private fun createBadgesTable(level: Int): Table {
        val tab = Table()

        val displayedBadges = getDisplayedBadges(level)

        if (displayedBadges.isNotEmpty()) {
            displayedBadges.chunked(BADGES_PER_ROW)
                .forEach { row ->
                    row.forEach { badge ->
                        tab.add(badge.image)
                            .size(28f, 28f)
                            .padRight(1f)
                            .padBottom(1f)
                    }
                    tab.row()
                }
        }

        return tab
    }

    private fun getDisplayedBadgesCount(level: Int): Int {
        val badgesCount = level / 10
        return Math.min(badgesCount, BADGES_COUNT)
    }

    private fun getDisplayedBadges(level: Int) = badges.take(getDisplayedBadgesCount(level))

    private fun getEarnedBadgeNumber(level: Int) = when {
        level > 10 * BADGES_COUNT -> 0
        level % 10 == 0 -> level / 10
        else -> 0
    }

    override fun dispose() {
        super.dispose()
        sheepTexture.dispose()
        wolfTexture.dispose()

        badges.forEach { it.dispose() }
    }
}


