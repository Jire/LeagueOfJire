package com.leagueofjire.game

import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.game.offsets.Offsets
import com.leagueofjire.util.free
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object GameUnitManager {
	
	private const val MAX_UNITS = 1024
	
	private var unitsRead = 0
	private val visitedNodes: LongSet = LongOpenHashSet(MAX_UNITS)
	
	// we have to use `LongArray` because `Array<Pointer>` would result in boxing
	private val unitScanPointers = LongArray(MAX_UNITS) {
		Pointer.alloc(0x30).address
	}
	
	private fun unitScanPointer(index: Int) = Pointer(unitScanPointers[index])
	
	val units: Int2ObjectMap<Unit> = Int2ObjectOpenHashMap(MAX_UNITS)
	
	val unitsIt = Int2ObjectMaps.fastIterable(units)
	
	val champions: ObjectList<Unit> = ObjectArrayList(10)
	val monsters: ObjectList<Unit> = ObjectArrayList(64)
	
	private val unitData = Pointer.alloc(0x4000)
	
	private fun scanUnits(process: AttachedProcess, objectManager: Pointer): Boolean {
		val numMissiles = objectManager.getInt(LViewOffsets.ObjectMapCount)
		if (numMissiles <= 0) return false
		
		val rootUnitAddress = objectManager.getInt(LViewOffsets.ObjectMapRoot).toLong()
		if (rootUnitAddress <= 0) return false
		
		unitsRead = 0
		champions.clear()
		monsters.clear()
		visitedNodes.clear()
		
		scanUnit(process, rootUnitAddress)
		
		return true
	}
	
	private tailrec fun scanUnit(process: AttachedProcess, address: Long) {
		val unitsRead = unitsRead++
		if (unitsRead >= MAX_UNITS || address <= 0 || visitedNodes.contains(address)) return
		
		visitedNodes.add(address)
		
		val data = unitScanPointer(unitsRead)
		if (!process.read(address, data, 0x30)) return
		if (!data.readable()) return
		
		val networkID = data.getInt(LViewOffsets.ObjectMapNodeNetId)
		if (networkID >= 0x40000000) {
			val unitAddress = data.getInt(LViewOffsets.ObjectMapNodeObject).toLong()
			updateUnit(process, networkID, unitAddress)
		}
		
		// children
		scanUnit(process, data.getInt(0).toLong())
		scanUnit(process, data.getInt(4).toLong())
		scanUnit(process, data.getInt(8).toLong())
	}
	
	private fun updateUnit(process: AttachedProcess, networkID: Int, address: Long) {
		if (address <= 0) return
		
		val unit: Unit
		if (units.containsKey(networkID)) {
			unit = units[networkID]
			if (unit.update(process, unitData, false) && networkID != unit.networkID)
				units[unit.networkID] = unit
		} else {
			unit = Unit(address)
			if (unit.update(process, unitData, true))
				units[unit.networkID] = unit
		}
		
		if (unit.isVisible)
			unit.lastVisibleAt = GameTime.time
		
		if (unit.networkID > 0) {
			val info = unit.info
			if (info.isChampion) {
				if (!champions.contains(unit))
					champions.add(unit)
				// add to champions list
			} else if (false/*data.isMinion*/) {
				// add to minions list
			} else if (info.isJungle) {
				if (!monsters.contains(unit))
					monsters.add(unit)
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