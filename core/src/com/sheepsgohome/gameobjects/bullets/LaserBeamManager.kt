package com.sheepsgohome.gameobjects.bullets

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World

object LaserBeamManager {

    private val laserBeams = mutableListOf<LaserBeam>()

    fun createSheepLaserBeam(world: World): LaserBeam {
        val beam = LaserBeam(world)
        laserBeams += beam
        return beam
    }

    fun updateSprites() = laserBeams.forEach { it.updateSprite() }

    fun drawLaserBeams(batch: SpriteBatch) = laserBeams.forEach { it.draw(batch) }

    fun cleanDisposed() = laserBeams.removeAll { it.disposed }

}