package com.leagueofjire.app

import com.badlogic.gdx.utils.ScreenUtils
import com.leagueofjire.game.*

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		val hook = LeagueOfLegendsHook.hook()
		
		UnitInfo.load()
		SpellInfo.load()
		
		val gameContext = DefaultGameContext(
			hook,
			GameTime,
			GameRenderer,
			GameMinimap,
			GameUnitManager,
			GameLocalPlayer,
			GameHoveredUnit
		)
		if (!gameContext.update()) return
		
		val overlay = Overlay()
		val scriptManager = ScriptManager(gameContext, overlay)
		
		overlay.open {
			scriptManager.load()
			renderHook {
				if (gameContext.update()) {
					ScreenUtils.clear(0F, 0F, 0F, 0F)
					scriptManager.render()
				}
			}
		}
	}
	
}