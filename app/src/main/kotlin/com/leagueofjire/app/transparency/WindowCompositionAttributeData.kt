package com.leagueofjire.app.transparency

import com.sun.jna.Pointer
import com.sun.jna.Structure

@Structure.FieldOrder(value = ["Attribute", "Data", "SizeOfData"])
class WindowCompositionAttributeData() : Structure(), Structure.ByReference {
	
	@JvmField
	internal var Attribute: Int = 0
	
	private var attribute: WindowCompositionAttributes
		get() = WindowCompositionAttributes[Attribute]
		set(value) {
			Attribute = value.id
		}
	
	@JvmField
	var Data: Pointer? = null
	
	@JvmField
	var SizeOfData: Int = 0
	
	constructor(
		Attribute: WindowCompositionAttributes = WindowCompositionAttributes.WCA_ACCENT_POLICY,
		AccentState: AccentStates = AccentStates.ACCENT_DISABLED,
		AccentFlags: Int = 0,
		GradientColor: Int = 0,
		AnimationId: Int = 0
	) : this() {
		val accent = AccentPolicy()
		accent.accentState = AccentState
		accent.AccentFlags = AccentFlags // must be 2 for transparency
		accent.GradientColor = GradientColor // ARGB color code for gradient
		accent.AnimationId = AnimationId
		val accentStructSize = accent.size()
		accent.write()
		val accentPtr = accent.pointer
		this.attribute = Attribute
		this.SizeOfData = accentStructSize
		this.Data = accentPtr
	}
	
}