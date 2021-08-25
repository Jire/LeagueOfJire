package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object LocalPlayer {
	
	lateinit var localPlayer: Unit
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val address = process.int(base.address + Offsets.LocalPlayer).toLong()
		if (address <= 0) return false
		
		val networkID = process.int(address + GameObject.ObjNetworkID)
		if (networkID <= 0) return false
		
		val unit = UnitManager.units[networkID] ?: return false
		localPlayer = unit
		
		return true
	}
	
}