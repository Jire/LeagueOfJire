package com.leagueofjire.core.game

import com.leagueofjire.core.LeagueOfLegendsHook

class DefaultGameContext(
	override val hook: LeagueOfLegendsHook,
	override val time: IGameTime,
	override val renderer: IGameRenderer,
	override val minimap: IGameMinimap,
	override val unitManager: IGameUnitManager,
	override val me: IGameLocalPlayer,
	override val hoveredUnit: IGameHoveredUnit
) : IGameContext {
	
	override fun update() = hook.run {
		time.update(process, baseModule)
				&& renderer.update(process, baseModule)
				&& minimap.update(process, baseModule)
				&& unitManager.update(process, baseModule)
				// clear missing objects
				&& me.update(process, baseModule)
				// && hoveredUnit.update(process, base)
				// get map, summoner's rift / howling etc.
	}
	
}