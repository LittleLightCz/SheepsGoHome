package com.sheepsgohome.gdx

import com.badlogic.gdx.scenes.scene2d.Actor
import com.sheepsgohome.gdx.listeners.clicked

fun Actor.onClick(action: () -> Unit) = addListener(clicked { action() })