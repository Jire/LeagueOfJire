package com.leagueofjire.game

import com.leagueofjire.ScreenPosition
import com.leagueofjire.game.unit.GameUnit

interface GameToScreenPosition {
	
	operator fun get(x: Float, y: Float, z: Float): ScreenPosition
	operator fun get(position: GamePosition): ScreenPosition = get(position.x, position.y, position.z)
	operator fun get(unit: GameUnit): ScreenPosition = get(unit.position)
	
}