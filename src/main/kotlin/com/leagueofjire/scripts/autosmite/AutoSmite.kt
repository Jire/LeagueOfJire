package com.leagueofjire.scripts.autosmite

import com.leagueofjire.input.Keyboard
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import java.awt.event.KeyEvent

class AutoSmite : Script() {
	
	override fun ScriptContext.setup() = unitHook {
		if (!isVisible || !isAlive || !info.isImportantJungle) return@unitHook
		
		val player = localPlayer.localPlayer
		if (!player.isAlive) return@unitHook
		
		val gameTime = gameTime.gameTime
		
		val smite = player.spells[5]
		if (health > smite.value || !smite.canCast(gameTime)/* || !withinDistance(player, smite.info.castRange)*/) return@unitHook
		
		if (!Keyboard.keyPressed(KeyEvent.VK_BACK_SPACE)) return@unitHook
		
		//println("AUTOSMITE $name (HP: $health)")
		mouse(renderer.screen(this)) {
			key(KeyEvent.VK_F)
		}
	}
	
}