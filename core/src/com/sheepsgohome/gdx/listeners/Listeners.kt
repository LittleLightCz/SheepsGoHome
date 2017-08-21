package com.sheepsgohome.gdx.listeners

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.sheepsgohome.shared.GameData
import com.sheepsgohome.shared.GameMusic

fun clicked(action: () -> Unit) = object : ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        action()
    }
}

fun changed(action: (event: ChangeListener.ChangeEvent, actor: Actor) -> Unit) = object : ChangeListener() {
    override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
        action(event, actor)
    }
}



