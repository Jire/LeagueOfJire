package com.leagueofjire.app.transparency
import com.sun.jna.Native

object DWM {

    @JvmStatic
    external fun DwmEnableBlurBehindWindow(hWnd: Long, pBlurBehind: DWM_BLURBEHIND): Int

    init {
        Native.register("Dwmapi")
    }

    const val DWM_BB_ENABLE = 0x00000001L
    const val DWM_BB_BLURREGION = 0x00000002L
    const val DWM_BB_TRANSITIONONMAXIMIZED = 0x00000004L

}