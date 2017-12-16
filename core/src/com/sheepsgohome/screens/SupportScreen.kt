package com.sheepsgohome.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.sheepsgohome.gdx.screens.switchToMainMenuScreen
import com.sheepsgohome.localization.Loc
import com.sheepsgohome.screens.parent.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.ui.BigSheepButton
import com.sheepsgohome.ui.ScreenTitle
import com.sheepsgohome.ui.SmallSheepButton
import com.sheepsgohome.ui.onClick

class SupportScreen : MenuScreen() {

    private val buttonRate = BigSheepButton(Loc.rate)
    private val buttonShareFB = BigSheepButton(Loc.shareOnFacebook)
    private val buttonShareGPlus = BigSheepButton(Loc.shareOnGoolePlus)
    private val buttonTweet = BigSheepButton(Loc.tweet)

    private val buttonBack = SmallSheepButton(Loc.back)

    private val title = ScreenTitle(Loc.howToSupport)

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

    }
}
