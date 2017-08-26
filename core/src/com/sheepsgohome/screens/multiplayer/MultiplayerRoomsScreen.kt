package com.sheepsgohome.screens.multiplayer

import com.sheepsgohome.screens.MenuScreen
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.ui.ScreenTitle

class MultiplayerRoomsScreen : MenuScreen() {

    private val title = ScreenTitle(GameData.loc.get("active.rooms"))

    init {
        table.add(title)
            .colspan(2)
            .center()
            .row()
    }

}