package com.leagueofjire.overlay.transparency

enum class WindowCompositionAttributes(val id: Int) {
	
	WCA_UNKNOWN(0),
	WCA_ACCENT_POLICY(19);
	
	companion object :
		EnumLookUpWithDefault<WindowCompositionAttributes>(
			values().associateBy(
				WindowCompositionAttributes::id
			), WCA_UNKNOWN
		)
	
}