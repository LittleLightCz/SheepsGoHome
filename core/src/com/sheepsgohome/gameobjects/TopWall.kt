package com.sheepsgohome.gameobjects

import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT

class TopWall(world: World) : Wall(world) {
    override fun updatePosition() {
        body.setTransform(0f, CAMERA_HEIGHT / 2, (Math.PI / 2f).toFloat())
    }
}

