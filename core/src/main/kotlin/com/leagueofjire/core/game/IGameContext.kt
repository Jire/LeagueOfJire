package com.leagueofjire.core.game

import com.leagueofjire.core.LeagueOfLegendsHook
import com.leagueofjire.game.GameContext

interface IGameContext : GameContext {
	
	val hook: LeagueOfLegendsHook
	override val time: IGameTime
	override val renderer: IGameRenderer
	override val minimap: IGameMinimap
	override val unitManager: IGameUnitManager
	override val me: IGameLocalPlayer
	val hoveredUnit: IGameHoveredUnit

}