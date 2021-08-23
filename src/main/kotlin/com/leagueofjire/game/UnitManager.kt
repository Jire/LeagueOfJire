package com.leagueofjire.game

import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.game.offsets.Offsets
import com.leagueofjire.util.free
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object UnitManager {
	
	private const val MAX_UNITS = 500
	
	private var unitReads = 0
	private val visitedNodes: LongSet = LongOpenHashSet(MAX_UNITS)
	
	val units: Int2ObjectMap<Unit> = Int2ObjectOpenHashMap(MAX_UNITS)
	
	private fun scanUnits(process: AttachedProcess, objectManager: Pointer): Boolean {
		val numMissiles = objectManager.getInt(LViewOffsets.ObjectMapCount)
		if (numMissiles <= 0) return false
		
		val rootUnitAddress = objectManager.getInt(LViewOffsets.ObjectMapRoot).toLong()
		if (rootUnitAddress <= 0) return false
		
		unitReads = 0
		visitedNodes.clear()
		
		scanUnit(process, rootUnitAddress)
		
		return true
	}
	
	private fun scanUnit(process: AttachedProcess, address: Long) {
		if (unitReads >= MAX_UNITS || address <= 0 || visitedNodes.contains(address)) return
		
		unitReads++
		visitedNodes.add(address)
		
		val data = process.readPointer(address, 0x30)
		if (!data.readable()) return
		
		for (childIndex in 0..2) {
			val childAddress = data.getInt(childIndex * 4L).toLong()
			if (childAddress > 0)
				scanUnit(process, childAddress)
		}
		
		val networkID = data.getInt(LViewOffsets.ObjectMapNodeNetId)
		if (networkID < 0x40000000) return
		
		val unitAddress = data.getInt(LViewOffsets.ObjectMapNodeObject).toLong()
		if (unitAddress <= 0) return
		
		updateUnit(process, networkID, unitAddress)
	}
	
	private val pointer = Pointer.alloc(0x4000)
	
	private fun updateUnit(process: AttachedProcess, networkID: Int, address: Long) {
		val unit: Unit
		if (units.containsKey(networkID)) {
			unit = units[networkID]
			if (unit.update(process, pointer, false) && networkID != unit.networkID)
				units.put(unit.networkID.toInt(), unit)
		} else {
			unit = Unit(address)
			if (unit.update(process, pointer, true))
				units.put(unit.networkID.toInt(), unit)
		}
		
		if (unit.isVisible)
			unit.lastVisibleAt = GameTime.gameTime
		
		if (false && unit.networkID > 0) {
			// indexToNetworkID[unit.objectIndex] = unit.networkID
			// updatedThisFrame.enqueue(unit.networkID)
			
			val info = unit.info
			if (info.isChampion) {
				// add to champions list
			} else if (false/*data.isMinion*/) {
				// add to minions list
			} else if (info.isJungle) {
				// add to jungle list
			} else if (false/*data.isTurret*/) {
				// add to turret list
			}
		}
	}
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val objectManagerOffset = process.int(base.address + Offsets.ObjectManager).toLong()
		if (objectManagerOffset <= 0) return false
		
		val objectManager = Pointer.alloc(256)
		if (!process.read(objectManagerOffset, objectManager, 256)) return false
		if (!objectManager.readable()) return false
		
		try {
			if (!scanUnits(process, objectManager)) return false
		} finally {
			objectManager.free(256)
		}
		
		return true
	}
	
}