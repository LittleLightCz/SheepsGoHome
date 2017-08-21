package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.google.common.collect.Iterables
import com.sheepsgohome.badges.Badge
import com.sheepsgohome.dialogs.NewBadgeDialog
import com.sheepsgohome.gdx.listeners.clicked
import com.sheepsgohome.gdx.screens.switchToGameplayClassicModeScreen
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.screens.GameResult.*
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.SOUND_VOLUME
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.shared.GameSounds.soundNewBadge
import com.sheepsgohome.shared.GameSounds.soundSheepSuccess
import com.sheepsgohome.shared.GameSounds.soundWolfFailure

class GameClassicModeResultScreen(private val gameResult: GameResult) : Screen {

    private val multiplier = 2f

    private val BADGES_COUNT = 12
    private val BUTTON_WIDTH = 80f

    private val stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))
    private val table = Table()

    private var buttonRetry: TextButton? = null
    private var buttonNext: TextButton? = null

    private val buttonQuit = TextButton(loc.get("quit"), skin).apply {
        addListener(clicked { switchToMainMenuScreen() })
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

    private val backgroundTexture = Texture("menu_background.png").apply {
        setFilter(Linear, Linear)
    }

    private val backgroundImage = Image(backgroundTexture).apply {
        width = CAMERA_WIDTH * multiplier
        height = CAMERA_HEIGHT * multiplier
    }

    private val badges = (1..BADGES_COUNT).map { Badge(it) }

    init {
        when(gameResult) {
            SHEEP_SUCCEEDED -> {
                buttonNext = TextButton(loc.get("next.level"), skin).apply {
                    addListener(clicked { switchToGameplayClassicModeScreen() })
                }
            }
            else -> {
                buttonRetry = TextButton(loc.get("retry"), skin).apply {
                    addListener(clicked { switchToGameplayClassicModeScreen() })
                }
            }
        }

        title = when (gameResult) {
            SHEEP_SUCCEEDED -> Label(loc.get("home.sweet.home"), skin, "menuTitle")
            else -> Label(loc.get("sheep.has.been.caught"), skin, "menuTitle")
        }

        val fontScale = (CAMERA_WIDTH * multiplier - 20) / title.prefWidth
        title.setFontScale(fontScale)
    }

    override fun show() {
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

        table.setFillParent(true)

        stage.addActor(backgroundImage)
        stage.addActor(table)

        if (showNewBadgeDialog) {
            badges.find { it.badgeNumber == badgeNumber }?.let {
                NewBadgeDialog(it).show(stage)
            }
        }

        Gdx.input.inputProcessor = stage

        if (SOUND_ENABLED) {
            sound.play(SOUND_VOLUME)
        }
    }

    private fun createBadgesTable(level: Int): Table {
        val tab = Table()

        val displayedBadges = getDisplayedBadges(level)

        if (displayedBadges.isNotEmpty()) {
            Iterables.partition(displayedBadges, 5)
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
        backgroundTexture.dispose()
        sheepTexture.dispose()
        wolfTexture.dispose()

        badges.forEach { it.dispose() }
    }
}


