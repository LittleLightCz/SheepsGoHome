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
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.shared.GameScreens
import com.sheepsgohome.shared.GameSkins.skin

class SupportScreen : Screen {
    private lateinit var stage: Stage
    private lateinit var table: Table

    private lateinit var buttonRate: TextButton
    private lateinit var buttonShareFB: TextButton
    private lateinit var buttonShareGPlus: TextButton
    private lateinit var buttonTweet: TextButton

    private lateinit var buttonBack: TextButton

    private lateinit var title: Label

    private lateinit var texture: Texture
    private lateinit var bgImage: Image

    override fun show() {
        buttonRate = TextButton(loc.get("rate"), skin)
        buttonShareFB = TextButton(loc.get("share.FB"), skin)
        buttonShareGPlus = TextButton(loc.get("share.GPlus"), skin)
        buttonTweet = TextButton(loc.get("tweet"), skin)

        buttonBack = TextButton(loc.get("back"), skin)
        title = Label(loc.get("how.to.support"), skin, "menuTitle")

        texture = Texture("menu_background.png")
        bgImage = Image(texture)

        val multiplier = 2f
        stage = Stage(StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier))

        table = Table()
        //        table.setDebug(true);

        //click listeners
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = GameScreens.mainMenuScreen
            }
        })

        buttonRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                GameData.androidFunctions.launchRateAppAction()
            }
        })

        buttonShareFB.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.net.openURI("https://www.facebook.com/sharer/sharer.php?u=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
            }
        })

        buttonShareGPlus.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.net.openURI("https://plus.google.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
            }
        })

        buttonTweet.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.net.openURI("https://twitter.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
            }
        })


        //table
        table.setFillParent(true)

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE)
        table.add<Label>(title).top().colspan(2).row()

        val contentTable = Table()
        contentTable.add<TextButton>(buttonRate).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        contentTable.add<TextButton>(buttonShareFB).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        contentTable.add<TextButton>(buttonShareGPlus).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()
        contentTable.add<TextButton>(buttonTweet).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row()

        table.add(contentTable).expand().top().row()

        table.add<TextButton>(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().center().row()

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
