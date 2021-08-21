package com.leagueofjire.input

import org.jire.kna.nativelib.windows.User32

object Keyboard {
	
	fun keyState(virtualKeyCode: Int) = User32.GetKeyState(virtualKeyCode)
	
	fun keyPressed(virtualKeyCode: Int) = keyState(virtualKeyCode) < 0
	
	fun keyReleased(virtualKeyCode: Int) = !keyPressed(virtualKeyCode)
	
}