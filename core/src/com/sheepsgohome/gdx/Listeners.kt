package com.sheepsgohome.gdx

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

fun clicked(action: () -> Unit) = object : ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        action()
    }
}

