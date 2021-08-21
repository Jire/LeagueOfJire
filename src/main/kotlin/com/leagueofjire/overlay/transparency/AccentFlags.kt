package com.leagueofjire.overlay.transparency

object AccentFlags {

    const val Transparent = 2
    const val DrawLeftBorder = 0x20
    const val DrawTopBorder = 0x40
    const val DrawRightBorder = 0x80
    const val DrawBottomBorder = 0x100
    const val DrawAllBorders = (DrawLeftBorder or DrawTopBorder or DrawRightBorder or DrawBottomBorder)

}