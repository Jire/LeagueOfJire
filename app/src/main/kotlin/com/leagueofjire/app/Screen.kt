package com.leagueofjire.app

import java.awt.Dimension
import java.awt.Toolkit

object Screen {

    private val DIMENSION: Dimension = Toolkit.getDefaultToolkit().screenSize

    val WIDTH = DIMENSION.width
    val HEIGHT = DIMENSION.height

    const val OVERLAY_OFFSET = 1
    val OVERLAY_WIDTH = WIDTH - OVERLAY_OFFSET
    val OVERLAY_HEIGHT = HEIGHT - OVERLAY_OFFSET

}