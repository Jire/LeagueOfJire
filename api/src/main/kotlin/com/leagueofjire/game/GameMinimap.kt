package com.leagueofjire.game

interface GameMinimap : GameState, GameToScreenPosition {
	
	val x: Float
	val y: Float
	
	val width: Float
	val height: Float
	
}