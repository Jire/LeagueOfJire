package com.leagueofjire.core.game

import com.leagueofjire.core.offsets.Offsets
import com.leagueofjire.game.GameTime
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.float

object IGameTime : GameTime {
	
	override var seconds = -1F
	
	fun update(process: AttachedProcess, baseModule: AttachedModule): Boolean {
		seconds = process.float(baseModule.address + Offsets.GameTime)
		return true
	}
	
	override fun update(): Boolean {
		TODO("Not yet implemented")
	}
	
}