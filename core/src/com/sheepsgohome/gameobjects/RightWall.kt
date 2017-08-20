package com.sheepsgohome.gameobjects

import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

class RightWall(world: World) : Wall(world) {
    override fun updatePosition() {
        body.setTransform(CAMERA_WIDTH / 2, 0f, 0f)
    }
}