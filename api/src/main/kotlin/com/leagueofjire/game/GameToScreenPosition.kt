package com.leagueofjire.game

import com.leagueofjire.ScreenPosition
import com.leagueofjire.game.unit.GameUnit

interface GameToScreenPosition {
	
	operator fun get(x: Float, y: Float, z: Float): ScreenPosition
	
}