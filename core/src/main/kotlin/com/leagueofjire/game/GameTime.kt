package com.leagueofjire.game

import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.float

object GameTime {
	
	@Volatile
	var time = 0F
	
	fun update(process: AttachedProcess, baseModule: AttachedModule): Boolean {
		time = process.float(baseModule.address + Offsets.GameTime)
		return true
	}
	
}