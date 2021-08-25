package com.leagueofjire.game

import com.leagueofjire.game.unit.GameUnit

interface GameUnitManager : GameState {
	
	operator fun <T : GameUnit> get(networkID: Int): T
	
}