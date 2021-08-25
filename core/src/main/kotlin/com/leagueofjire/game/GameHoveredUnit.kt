package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object GameHoveredUnit {
	
	@Volatile
	var hoveredUnit: Unit? = null
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val address = process.int(base.address + Offsets.UnderMouseObject).toLong()
		if (address <= 0) return false
		
		val networkID = process.int(address + GameObject.ObjNetworkID)
		if (networkID < 0) return false
		
		hoveredUnit = GameUnitManager.units[networkID]
		return true
	}
	
}