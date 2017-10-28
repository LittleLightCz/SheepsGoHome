package com.sheepsgohome.positioning

import com.badlogic.gdx.physics.box2d.Body
import com.sheepsgohome.shared.GameData.CAMERA_WIDTH

class BodyPositioner {

    fun alignInCenteredGrid(
            bodies: List<Body>,
            maxBodiesInRow: Int,
            columnSize: Float,
            verticalOffset: Float
    ) {

        var row = 0

        bodies.chunked(maxBodiesInRow).forEach { bodiesRow ->
            var column = 1
            val rowWidth = bodiesRow.size * columnSize
            val centeredOffset = (CAMERA_WIDTH / 2) + (columnSize / 2) - (CAMERA_WIDTH - rowWidth) / 2

            bodiesRow.forEach {
                it.setTransform(
                        columnSize * column - centeredOffset,
                        (-columnSize * row) + verticalOffset,
                        0f
                )
                column++
            }
            row++
        }
    }
}