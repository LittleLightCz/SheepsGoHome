package com.sheepsgohome.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Sound
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
import com.sheepsgohome.dialogs.NewBadgeDialog
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.SOUND_ENABLED
import com.sheepsgohome.shared.GameData.SOUND_VOLUME
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin
import com.sheepsgohome.shared.GameSounds
import java.util.*

class GameplayDialog(private val type: GameDialogType) : Screen {

    private val BADGES_COUNT = 12
    private val BUTTON_WIDTH = 80f

    private val stage: Stage
    private val table: Table

    private var buttonRetry: TextButton? = null
    private var buttonNext: TextButton? = null
    private val buttonQuit: TextButton
    private val title: Label

    private val sheep_texture: Texture
    private val wolf_texture: Texture
    private val texture: Texture

    private var imgSheep: Image? = null
    private var imgWolf: Image? = null
    private val bgImage: Image

    private var sound: Sound? = null

    private val badges: Vector<Texture>

    init {
        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))
        table = Table()

        sheep_texture = Texture("sheep_success.png")
        sheep_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)


        wolf_texture = when (type) {
            GameDialogType.typeSheepFailed_Alpha -> Texture("wolf_alpha_fail.png")
            GameDialogType.typeSheepFailed_Hungry -> Texture("wolf_hungry_fail.png")
            else -> Texture("wolf_fail.png")
        }
        wolf_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        badges = Vector()
        //fill vector
        for (i in 0..BADGES_COUNT - 1) {
            val t = Texture("badges/badge" + (i + 1) + ".png")
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear)
            badges.add(t)
        }

        if (type !== GameDialogType.typeSheepSucceeded) {
            buttonRetry = TextButton(loc.get("retry"), skin)
            imgWolf = Image(wolf_texture)

            buttonRetry?.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    (Gdx.app.applicationListener as Game).screen = GameScreens.gameplayClassicModeScreen
                }
            })

        } else {
            buttonNext = TextButton(loc.get("next.level"), skin)
            imgSheep = Image(sheep_texture)

            buttonNext?.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    (Gdx.app.applicationListener as Game).screen = GameScreens.gameplayClassicModeScreen
                }
            })

        }

        buttonQuit = TextButton(loc.get("quit"), skin)

        buttonQuit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = GameScreens.mainMenuScreen
            }
        })

        //---------------------------------

        texture = Texture("menu_background.png")
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)

        bgImage = Image(texture)
        bgImage.width = CAMERA_WIDTH * multiplier
        bgImage.height = CAMERA_HEIGHT * multiplier

        title = when (type) {
            GameDialogType.typeSheepSucceeded -> Label(loc.get("home.sweet.home"), skin, "menuTitle")
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

        sound = null

        if (type !== GameDialogType.typeSheepSucceeded) {

            sound = GameSounds.soundWolfFailure

            imgWolf?.let {
                val mult = CAMERA_WIDTH * 2 / it.prefWidth * 0.95f

                table.add(it)
                    .size(it.width * mult, it.height * mult)
                    .colspan(2)
                    .expand()
                    .row()
            }

            table.add(buttonRetry).size(BUTTON_WIDTH, BUTTON_WIDTH / 2)

        } else {
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


        table.add(buttonQuit).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        table.setFillParent(true)

        stage.addActor(bgImage)
        stage.addActor(table)

        if (showNewBadgeDialog && badgeNo > 0) {
            NewBadgeDialog(getBadgeName(badgeNo), badges[badgeNo - 1], skin, "dialog").show(stage)
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
        texture.dispose()
        sheep_texture.dispose()
        wolf_texture.dispose()

        //dispose vector textures
        val it = badges.iterator()
        while (it.hasNext()) {
            it.next().dispose()
        }
        badges.clear()
    }
}


