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
import com.sheepsgohome.dialogs.NewBadgeDialog
import com.sheepsgohome.gdx.clicked
import com.sheepsgohome.screens.GameResult.*
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.SOUND_VOLUME
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.shared.GameSounds
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
        addListener(clicked { GameScreens.switchScreen(GameScreens.mainMenuScreen) })
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

    private val backgroundTexture = Texture("menu_background.png").apply {
        setFilter(Linear, Linear)
    }

    private val backgroundImage = Image(backgroundTexture).apply {
        width = CAMERA_WIDTH * multiplier
        height = CAMERA_HEIGHT * multiplier
    }

    private var imgSheep: Image? = null
    private var imgWolf: Image? = null

    private val badges = (1..BADGES_COUNT).map { badgeNumber ->
        Texture("badges/badge$badgeNumber.png").apply {
            setFilter(Linear, Linear)
        }
    }

    init {
        when(gameResult) {
            SHEEP_SUCCEEDED -> {
                buttonNext = TextButton(loc.get("next.level"), skin).apply {
                    addListener(clicked { GameScreens.switchScreen(GameScreens.gameplayClassicModeScreen) })
                }

                imgSheep = Image(sheepTexture)
            }
            else -> {
                buttonRetry = TextButton(loc.get("retry"), skin).apply {
                    addListener(clicked { GameScreens.switchScreen(GameScreens.gameplayClassicModeScreen) })
                }

                imgWolf = Image(wolfTexture)
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
        var badgeNo = 0

        var sound: Sound?

        when(gameResult) {
            SHEEP_SUCCEEDED -> {
                //has earned new badge?
                badgeNo = getEarnedBadge(GameData.LEVEL)
                if (badgeNo > 0) {
                    showNewBadgeDialog = true
                }

                if (showNewBadgeDialog) {
                    sound = GameSounds.soundNewBadge
                } else {
                    sound = GameSounds.soundSheepSuccess
                }

                //display badges
                table.add(createBadgesTable(GameData.LEVEL)).expandX().colspan(2).row()

                GameData.levelUp()

                val mult = 0.30f

                imgSheep?.let {
                    table.add(it)
                            .size(it.width * mult, it.height * mult)
                            .colspan(2)
                            .expand()
                            .row()
                }

                table.add(buttonNext).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
            }
            else -> {
                sound = soundWolfFailure

                imgWolf?.let {
                    val mult = CAMERA_WIDTH * 2 / it.prefWidth * 0.95f

                    table.add(it)
                            .size(it.width * mult, it.height * mult)
                            .colspan(2)
                            .expand()
                            .row()
                }

                table.add(buttonRetry).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
            }
        }

        table.add(buttonQuit)
                .size(BUTTON_WIDTH, BUTTON_WIDTH / 2)
                .row()

        table.setFillParent(true)

        stage.addActor(backgroundImage)
        stage.addActor(table)

        if (showNewBadgeDialog && badgeNo > 0) {
            NewBadgeDialog(getBadgeName(badgeNo), badges[badgeNo - 1]).show(stage)
        }

        Gdx.input.inputProcessor = stage

        if (SOUND_ENABLED) {
            sound?.play(SOUND_VOLUME)
        }
    }

    private fun getBadgeName(badgeNo: Int): String {
        when (badgeNo) {
            1 -> return loc.get("pasture")
            2 -> return loc.get("threat.awareness")
            3 -> return loc.get("alpha.defeater")
            4 -> return loc.get("escapist")
            5 -> return loc.get("agility")
            6 -> return loc.get("tactician")
            7 -> return loc.get("fearless")
            8 -> return loc.get("sheeps.defiance")
            9 -> return loc.get("limitless.courage")
            10 -> return loc.get("ruthless.conspiracy")
            11 -> return loc.get("wolf.apocalypse")
            12 -> return loc.get("sheep.master")
            else -> return ""
        }
    }

    private fun createBadgesTable(level: Int): Table {
        val tab = Table()

        val badgesCount = getBadgesCount(level)
        for (i in 0..badgesCount - 1) {
            tab.add(Image(badges[i])).size(28f, 28f).padRight(1f)
            if (i == 5) {
                tab.row()
            }
        }

        return tab
    }

    private fun getBadgesCount(level: Int): Int {
        val ret = level / 10
        return if (ret > BADGES_COUNT) BADGES_COUNT else ret
    }

    private fun getEarnedBadge(level: Int): Int {
        if (level > 10 * BADGES_COUNT) {
            return 0
        }

        return if (level % 10 == 0) getBadgesCount(level) else 0
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
        backgroundTexture.dispose()
        sheepTexture.dispose()
        wolfTexture.dispose()

        //dispose vector textures
        badges.forEach { it.dispose() }
    }
}


