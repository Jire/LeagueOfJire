package com.leagueofjire.game

import com.leagueofjire.overlay.Overlay

object GameContext {
	
	fun update(): Boolean {
		val process = Overlay.process
		val base = Overlay.base
		return GameTime.update(process, base)
				&& Renderer.update(process, base)
				&& Minimap.update(process, base)
				&& UnitManager.update(process, base)
				// clear missing objects
				&& LocalPlayer.update(process, base)
		// && hoveredUnit.update(process, base)
		// get map, summoner's rift / howling etc.
	}
	
}