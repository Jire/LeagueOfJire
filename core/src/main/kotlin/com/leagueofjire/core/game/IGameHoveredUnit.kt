package com.leagueofjire.core.game

import com.leagueofjire.core.game.unit.IGameUnit
import com.leagueofjire.core.offsets.GameObject
import com.leagueofjire.core.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object IGameHoveredUnit {
	
	@Volatile
	var hoveredUnit: IGameUnit? = null
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val address = process.int(base.address + Offsets.UnderMouseObject).toLong()
		if (address <= 0) return false
		
		val networkID = process.int(address + GameObject.ObjNetworkID)
		if (networkID < 0) return false
		
		hoveredUnit = IGameUnitManager.units[networkID]
		return true
	}
	
}