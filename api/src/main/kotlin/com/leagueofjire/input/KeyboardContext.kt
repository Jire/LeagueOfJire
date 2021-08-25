package com.leagueofjire.input

interface KeyboardContext {
	
	fun state(keyCode: Int): Int
	
	fun pressed(keyCode: Int): Boolean
	
	fun released(keyCode: Int): Boolean
	
	fun press(keyCode: Int)
	
	fun release(keyCode: Int)
	
	fun pressAndRelease(keyCode: Int) {
		press(keyCode)
		release(keyCode)
	}
	
}