package com.leagueofjire.core.game

import com.leagueofjire.core.offsets.LViewOffsets
import com.leagueofjire.game.GameTime
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.float

object IGameTime : GameTime {
	
	override var seconds = -1F
	
	fun update(process: AttachedProcess, baseModule: AttachedModule): Boolean {
		seconds = process.float(baseModule.address + LViewOffsets.GameTime)
		return true
	}
	
	override fun update(): Boolean {
		TODO("Not yet implemented")
	}
	
}