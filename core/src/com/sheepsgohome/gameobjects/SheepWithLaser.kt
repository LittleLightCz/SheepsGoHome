package com.sheepsgohome.gameobjects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.gameobjects.bullets.LaserBeamManager

class SheepWithLaser(val world: World): Sheep(world) {

    private val angleOffset = Math.toRadians(-90.0).toFloat()
    private val distanceFromSheepCoef = SHEEP_SIZE * 0.7f

    fun shoot() {
        with(sprite) {
            val direction = Vector2()
            steerableBody.angleToVector(direction, steerableBody.body.angle + angleOffset)

            val laserBeam = LaserBeamManager.createSheepLaserBeam(world)

            laserBeam.setPosition(
                    x + (SHEEP_SIZE / 2) + (direction.x * distanceFromSheepCoef),
                    y + (SHEEP_SIZE / 2) + (direction.y * distanceFromSheepCoef)
            )

            laserBeam.shootInDirection(direction)
        }
    }
}