package com.leagueofjire.game

import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.float

object GameTime {
	
	@Volatile
	var gameTime = 0F
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		gameTime = process.float(base.address + Offsets.GameTime)
		return true
	}
	
}