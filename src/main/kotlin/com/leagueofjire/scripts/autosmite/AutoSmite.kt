package com.leagueofjire.scripts.autosmite

import com.leagueofjire.game.Renderer
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.KeyEvent

class AutoSmite : Script() {
	
	val robot = Robot()
	
	override fun ScriptContext.run() = Overlay {
		if (!isVisible || !isAlive || !info.isImportantJungle || health > 450) return@Overlay
		
		println("AUTOSMITE $name (HP: $health)")
		
		val (sx, sy) = Renderer.worldToScreen(x, y, z)
		
		val original = MouseInfo.getPointerInfo().location
		robot.mouseMove(sx.toInt(), sy.toInt())
		robot.keyPress(KeyEvent.VK_F)
		robot.keyRelease(KeyEvent.VK_F)
		robot.mouseMove(original.x, original.y)
	}
	
}