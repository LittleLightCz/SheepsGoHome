package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameData.loc
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class SupportScreen : MenuScreen() {

    private val buttonRate = BigSheepButton(loc.get("rate"))
    private val buttonShareFB = BigSheepButton(loc.get("share.FB"))
    private val buttonShareGPlus = BigSheepButton(loc.get("share.GPlus"))
    private val buttonTweet = BigSheepButton(loc.get("tweet"))

    private val buttonBack = SmallSheepButton(loc.get("back"))

    private val title = ScreenTitle(loc.get("how.to.support"))

    init {

        buttonBack.onClick {
            switchToMainMenuScreen()
        }

        buttonRate.onClick {
            GameData.android?.launchRateAppAction()
        }

        buttonShareFB.onClick {
            Gdx.net.openURI("https://www.facebook.com/sharer/sharer.php?u=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
        }

        buttonShareGPlus.onClick {
            Gdx.net.openURI("https://plus.google.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
        }

        buttonTweet.onClick {
            Gdx.net.openURI("https://twitter.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android")
        }

        table.add(title)
                .top()
                .colspan(2)
                .row()

        val contentTable = Table()
        buttonRate.addTo(contentTable).row()
        buttonShareFB.addTo(contentTable).row()
        buttonShareGPlus.addTo(contentTable).row()
        buttonTweet.addTo(contentTable).row()

        table.add(contentTable)
                .expand()
                .top()
                .row()

        buttonBack.addTo(table)
                .bottom()
                .center()
                .row()

        Gdx.input.inputProcessor = stage
    }
}
