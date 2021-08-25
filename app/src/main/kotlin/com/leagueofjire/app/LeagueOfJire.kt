package com.leagueofjire.app

import com.leagueofjire.overlay.OverlayManager

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		OverlayManager.open()
		ScriptManager.load()
	}
	
}