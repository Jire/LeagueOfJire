package com.leagueofjire.game

import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess

class DefaultGameContext(
	override val hook: LeagueOfLegendsHook,
	override val gameTime: GameTime,
	override val renderer: GameRenderer,
	override val minimap: GameMinimap,
	override val unitManager: GameUnitManager,
	override val localPlayer: GameLocalPlayer,
	override val hoveredUnit: GameHoveredUnit
) : GameContext {
	
	fun update(process: AttachedProcess = hook.process, baseModule: AttachedModule = hook.baseModule) =
		gameTime.update(process, baseModule)
				&& renderer.update(process, baseModule)
				&& minimap.update(process, baseModule)
				&& unitManager.update(process, baseModule)
				// clear missing objects
				&& localPlayer.update(process, baseModule)
				// && hoveredUnit.update(process, base)
				// get map, summoner's rift / howling etc.
	
}