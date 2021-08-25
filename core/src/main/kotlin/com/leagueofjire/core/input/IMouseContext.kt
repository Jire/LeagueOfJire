package com.leagueofjire.core.input

import com.leagueofjire.ScreenPosition
import com.leagueofjire.input.MouseContext
import java.awt.MouseInfo
import java.awt.Robot

class IMouseContext(val robot: Robot) : MouseContext {
	
	override fun move(x: Int, y: Int) = robot.mouseMove(x, y)
	
	override val position get() = MouseInfo.getPointerInfo().location.run { ScreenPosition(x, y) }
	
}