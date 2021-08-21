package com.leagueofjire.game

import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.float
import org.jire.leagueofjire.offsets.Offsets

object GameTime {
	
	@Volatile
	var gameTime = 0F
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		gameTime = process.float(base.address + Offsets.GameTime)
		return true
	}
	
}