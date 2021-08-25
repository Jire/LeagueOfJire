package com.leagueofjire.game

interface GameContext {
	
	val hook: LeagueOfLegendsHook
	val gameTime: GameTime
	val renderer: GameRenderer
	val minimap: GameMinimap
	val unitManager: GameUnitManager
	val localPlayer: GameLocalPlayer
	val hoveredUnit: GameHoveredUnit

}