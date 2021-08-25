package com.leagueofjire.core.input

import com.leagueofjire.input.KeyboardContext
import org.jire.kna.nativelib.windows.User32
import java.awt.Robot

class IKeyboardContext(val robot: Robot) : KeyboardContext {
	
	override fun state(keyCode: Int) = User32.GetKeyState(keyCode).toInt()
	
	override fun pressed(keyCode: Int) = state(keyCode) < 0
	
	override fun released(keyCode: Int) = !pressed(keyCode)
	
	override fun press(keyCode: Int) = robot.keyPress(keyCode)
	
	override fun release(keyCode: Int) = robot.keyRelease(keyCode)
	
}