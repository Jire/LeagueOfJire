package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object LocalPlayer {
	
	lateinit var champion: Champion
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val playerAddress = process.int(base.address + Offsets.LocalPlayer).toLong()
		if (playerAddress <= 0) return false
		
		val networkID = process.int(playerAddress + GameObject.ObjNetworkID)
		if (networkID < 0) return false
		
		val unit = UnitManager.objectMap[networkID] ?: return false
		champion = if (unit is Champion) {
			unit
		} else {
			println("our player wasn't champion... fixing that rn")
			unit.transformToChampion(process)
		}
		return true
	}
	
}