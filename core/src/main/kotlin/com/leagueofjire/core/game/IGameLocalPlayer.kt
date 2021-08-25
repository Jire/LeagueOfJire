package com.leagueofjire.core.game

import com.leagueofjire.core.game.unit.IGameUnit
import com.leagueofjire.core.offsets.GameObject
import com.leagueofjire.core.offsets.Offsets
import com.leagueofjire.game.unit.champion.GameChampionMe
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object IGameLocalPlayer : GameChampionMe, IGameUnit() {
	
	lateinit var localPlayer: IGameUnit
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val address = process.int(base.address + Offsets.LocalPlayer).toLong()
		if (address <= 0) return false
		
		val networkID = process.int(address + GameObject.ObjNetworkID)
		if (networkID <= 0) return false
		
		val unit = IGameUnitManager.units[networkID] ?: return false
		localPlayer = unit
		
		return true
	}
	
}