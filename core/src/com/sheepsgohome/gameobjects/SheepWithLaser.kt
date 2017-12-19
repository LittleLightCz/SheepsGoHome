package com.sheepsgohome.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.sheepsgohome.gameobjects.bullets.LaserBeamManager

class SheepWithLaser(val world: World): Sheep(world) {

    private val LASER_SIZE = SHEEP_SIZE

    private val angleOffset = Math.toRadians(-90.0).toFloat()
    private val distanceFromSheepCoef = SHEEP_SIZE * 0.7f

    var lives = 3
        private set

    val laserTextute = Texture("sheepLaser.png")

    val laserSprite = Sprite(laserTextute).apply {
        setSize(LASER_SIZE, LASER_SIZE)
        setOriginCenter()
    }

    fun shoot() {
        with(sheepSprite) {
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

    fun hitByLaserBeam() {
        lives--

        val newAlpha = lives / 3f
        sheepSprite.setAlpha(newAlpha)
        laserSprite.setAlpha(newAlpha)
    }

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        laserSprite.draw(batch)
    }

    override fun updateSprite() {
        super.updateSprite()
        updateSprite(laserSprite, LASER_SIZE)
    }

    override fun dispose() {
        super.dispose()
        laserTextute.dispose()
    }
}