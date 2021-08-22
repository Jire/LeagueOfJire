package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object HoveredObject {

	@Volatile
	var hovered: Unit? = null
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val addrObj = process.int(base.address + Offsets.UnderMouseObject).toLong()
		if (addrObj <= 0) return false
		
		val networkID = process.int(addrObj + GameObject.ObjNetworkID)
		if (networkID < 0) return false
		
		hovered = UnitManager.units[networkID]
		return true
	}

}