package com.leagueofjire.app

import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.overlay.OverlayManager
import com.leagueofjire.scripts.ScriptContext

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		OverlayManager.open()
		val scriptContext =
			ScriptContext(Overlay, GameTime, Renderer, Minimap, UnitManager, LocalPlayer, HoveredUnit).apply { load() }
		Overlay.renderHook {
			scriptContext.render()
		}
	}
	
}