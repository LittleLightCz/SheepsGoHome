package com.sheepsgohome.gameobjects.walls

import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData.CAMERA_HEIGHT

class BottomWall(world: World) : Wall(world) {
    override fun setDefaultPosition() {
        body.setTransform(0f, -CAMERA_HEIGHT / 2, (Math.PI / 2).toFloat())
    }
}