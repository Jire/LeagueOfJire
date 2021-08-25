package com.leagueofjire.game

interface GameRenderer : GameState, GameToScreenPosition {
	
	val width: Int
	val height: Int
	
}