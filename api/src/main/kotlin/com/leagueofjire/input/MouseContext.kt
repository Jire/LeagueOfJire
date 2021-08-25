package com.leagueofjire.input

import com.leagueofjire.ScreenPosition

interface MouseContext {
	
	fun move(x: Int, y: Int)
	
	fun move(position: ScreenPosition) = move(position.x, position.y)
	
	val position: ScreenPosition
	
}