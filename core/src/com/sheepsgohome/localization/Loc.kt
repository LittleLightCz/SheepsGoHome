package com.sheepsgohome.localization

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle
import java.util.*

object Loc {

    private val loc: I18NBundle by lazy {
        val baseFileHandle = Gdx.files.internal("loc/Language")
        val locale = Locale.getDefault()
        I18NBundle.createBundle(baseFileHandle, locale)
    }

    /**
     * Localized text functions
     */

    fun level(level: Int) = loc.format("level", level)

    val agility get() = loc.get("agility")
    val alphaDefeater get() = loc.get("alpha.defeater")
    val back get() = loc.get("back")
    val cancel get() = loc.get("cancel")
    val chooseMode get() = loc.get("choose.mode")
    val classic get() = loc.get("classic")
    val connectingToGoogle get() = loc.get("connecting.to.google")
    val connectionFailed get() = loc.get("connection.failed")
    val controls get() = loc.get("controls")
    val create get() = loc.get("create")
    val creatingMultiplayerGame get() = loc.get("creating.multiplayer.game")
    val downloadingData get() = loc.get("downloading.data")
    val escapist get() = loc.get("escapist")
    val exit get() = loc.get("exit")
    val fearless get() = loc.get("fearless")
    val homeSweetHome get() = loc.get("home.sweet.home")
    val howToSupport get() = loc.get("how.to.support")
    val lasers get() = loc.get("lasers")
    val leaderboard get() = loc.get("leaderboard")
    val left get() = loc.get("left")
    val levelHeading get() = loc.get("level.heading")
    val limitlessCourage get() = loc.get("limitless.courage")
    val multiplayer get() = loc.get("multiplayer")
    val newBadgeEarned get() = loc.get("new.badge.earned")
    val nextLevel get() = loc.get("next.level")
    val no get() = loc.get("no")
    val none get() = loc.get("none")
    val ok get() = loc.get("ok")
    val pasture get() = loc.get("pasture")
    val play get() = loc.get("play")
    val player get() = loc.get("player")
    val quit get() = loc.get("quit")
    val rate get() = loc.get("rate")
    val retry get() = loc.get("retry")
    val right get() = loc.get("right")
    val ruthlessConspiracy get() = loc.get("ruthless.conspiracy")
    val save get() = loc.get("save")
    val search get() = loc.get("search")
    val searchingForMultiplayerGames get() = loc.get("searching.for.multiplayer.games")
    val settings get() = loc.get("settings")
    val shareOnFacebook get() = loc.get("share.FB")
    val shareOnGoolePlus get() = loc.get("share.GPlus")
    val sheepHasBeenCaught get() = loc.get("sheep.has.been.caught")
    val sheepMaster get() = loc.get("sheep.master")
    val sheepsDefiance get() = loc.get("sheeps.defiance")
    val sheepsGoHome get() = loc.get("sheeps.go.home")
    val sound get() = loc.get("sound")
    val soundEnabled get() = loc.get("sound.enabled")
    val tactician get() = loc.get("tactician")
    val threatAwareness get() = loc.get("threat.awareness")
    val tweet get() = loc.get("tweet")
    val virtualJoystick get() = loc.get("virtual.joystick")
    val wolfApocalypse get() = loc.get("wolf.apocalypse")
    val yes get() = loc.get("yes")
    val yourPositionIs get() = loc.get("your.position.is")
    val yourRankIsNotPublic get() = loc.get("your.rank.is.not.public")
    val sheepBurnedToAshes get() = loc.get("sheep.burned.to.ashes")

}