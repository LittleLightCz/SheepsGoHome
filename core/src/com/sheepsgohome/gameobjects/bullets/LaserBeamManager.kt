package com.sheepsgohome.gameobjects.bullets

import com.badlogic.gdx.physics.box2d.World

object LaserBeamManager {

    private val laserBeams = mutableListOf<LaserBeam>()

    fun createSheepLaserBeam(world: World): LaserBeam {
        val beam = LaserBeam(world)
        laserBeams += beam
        return beam
    }

    fun cleanDisposed() = laserBeams.removeAll { it.disposed }

}