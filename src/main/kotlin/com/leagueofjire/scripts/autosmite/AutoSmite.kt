package com.leagueofjire.scripts.autosmite

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.input.Keyboard
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import java.awt.event.KeyEvent

class AutoSmite(
	val toggleKey: Int = KeyEvent.VK_BACK_SPACE
) : Script() {
	
	var lastToggle = -1F
	var enabled = false
	
	override fun ScriptContext.setup() = unitHook {
		if (!enabled || !isVisible || !isAlive || !info.isImportantJungle) return@unitHook
		
		val player = localPlayer.localPlayer
		if (!player.isAlive) return@unitHook
		
		val gameTime = gameTime.gameTime
		
		val smite = player.spells[5]
		if (health > smite.value || !smite.canCast(gameTime)/* || !withinDistance(player, smite.info.castRange)*/) return@unitHook
		
		mouse(renderer.screen(this)) {
			key(KeyEvent.VK_F)
		}
	}
	
	override fun ScriptContext.run() {
		val time = gameTime.gameTime
		if (time - lastToggle >= 0.3F && Keyboard.keyPressed(KeyEvent.VK_BACK_SPACE)) {
			lastToggle = time
			enabled = !enabled
		}
		
		val color = if (enabled) Color.GREEN else Color.RED
		overlay.shapes.color = color
		val y = renderer.height / 2F
		overlay.shapes.rect(2F, y - 2F, 72F, 18F)
		overlay.texts.color = color
		overlay.texts.text("AutoSmite", 5F, y)
	}
	
}