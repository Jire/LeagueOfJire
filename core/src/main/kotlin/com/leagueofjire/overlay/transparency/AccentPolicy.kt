package com.leagueofjire.overlay.transparency
import com.sun.jna.Structure

@Structure.FieldOrder(value = ["AccentState", "AccentFlags", "GradientColor", "AnimationId"])
class AccentPolicy : Structure(), Structure.ByReference {
    @JvmField
    internal var AccentState: Int = 0

    var accentState
        get() = AccentStates[AccentState]
        set(value) {
            AccentState = value.ordinal
        }

    @JvmField
    var AccentFlags: Int = 0

    @JvmField
    var GradientColor: Int = 0

    @JvmField
    var AnimationId: Int = 0
}