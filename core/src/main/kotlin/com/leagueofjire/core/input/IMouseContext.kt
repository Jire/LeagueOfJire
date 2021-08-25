package com.leagueofjire.core.input

import com.leagueofjire.ScreenPosition
import com.leagueofjire.core.game.Vector2D
import com.leagueofjire.input.MouseContext
import java.awt.MouseInfo
import java.awt.Robot

class IMouseContext(val robot: Robot) : MouseContext {
	
	override fun move(x: Int, y: Int) = robot.mouseMove(x, y)
	
	override val position: ScreenPosition
		get() {
			val point = MouseInfo.getPointerInfo().location
			return Vector2D(point.x, point.y)
		}
	
}