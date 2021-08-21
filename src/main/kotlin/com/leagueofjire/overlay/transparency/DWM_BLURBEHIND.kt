package com.leagueofjire.overlay.transparency
import com.sun.jna.Structure

@Structure.FieldOrder(value = ["dwFlags", "fEnable", "hRgnBlur", "fTransitionOnMaximized"])
class DWM_BLURBEHIND : Structure() {

    @JvmField
    var dwFlags: Long? = null

    @JvmField
    var fEnable = false

    @JvmField
    var hRgnBlur: Long? = null

    @JvmField
    var fTransitionOnMaximized = false

}