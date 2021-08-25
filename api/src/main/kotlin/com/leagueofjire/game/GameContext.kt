package com.leagueofjire.game

import com.leagueofjire.game.unit.champion.GameChampionMe

interface GameContext : GameState {
	
	val time: GameTime
	val renderer: GameRenderer
	val minimap: GameMinimap
	val unitManager: GameUnitManager
	val me: GameChampionMe
	
}