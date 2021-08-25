package com.leagueofjire.app

import com.badlogic.gdx.utils.ScreenUtils
import com.leagueofjire.core.game.*
import com.leagueofjire.core.LeagueOfLegendsHook
import com.leagueofjire.core.game.unit.champion.spell.SpellInfo
import com.leagueofjire.core.game.unit.UnitInfo

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		val hook = LeagueOfLegendsHook.hook()
		
		UnitInfo.load()
		SpellInfo.load()
		
		val gameContext = DefaultGameContext(
			hook,
			IGameTime,
			IGameRenderer,
			IGameMinimap,
			IGameUnitManager,
			IGameLocalPlayer,
			IGameHoveredUnit
		)
		if (!gameContext.update()) return
		
		val overlay = Overlay(gameContext)
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