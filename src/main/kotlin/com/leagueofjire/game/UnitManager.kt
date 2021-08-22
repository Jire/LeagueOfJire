package com.leagueofjire.game

import com.badlogic.gdx.utils.LongQueue
import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.game.offsets.Offsets
import com.leagueofjire.util.free
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.*
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import kotlin.math.abs

object UnitManager {
	
	private const val MAX_OBJECTS = 500
	
	val unitAddresses = LongArray(MAX_OBJECTS)
	
	val nodesToVisit: LongQueue = LongQueue(MAX_OBJECTS)
	val visitedNodes: LongSet = LongOpenHashSet(MAX_OBJECTS)
	
	val units: Int2ObjectMap<Unit> = Int2ObjectOpenHashMap(MAX_OBJECTS)
	
	private fun scanUnits(process: AttachedProcess, objectManager: Pointer): Int {
		val numMissiles = objectManager.getInt(LViewOffsets.ObjectMapCount)
		if (numMissiles <= 0) return 0
		
		val rootNode = objectManager.getInt(LViewOffsets.ObjectMapRoot).toLong()
		if (rootNode <= 0) return 0
		
		nodesToVisit.clear()
		visitedNodes.clear()
		
		nodesToVisit.addFirst(rootNode)
		
		var unitReads = 0
		var unitCount = 0
		
		while (unitReads < MAX_OBJECTS && !nodesToVisit.isEmpty) {
			val node = nodesToVisit.removeLast()
			if (node <= 0 || visitedNodes.contains(node)) continue
			
			unitReads++
			visitedNodes.add(node)
			
			val data = process.readPointer(node, 0x30)
			if (!data.readable()) continue
			
			for (childIndex in 0..2) {
				val childAddress = data.getInt(childIndex * 4L).toLong()
				if (childAddress > 0)
					nodesToVisit.addLast(childAddress)
			}
			
			// Network ids of the objects we are interested in start from 0x40000000. We do this check for performance reasons.
			val networkID = data.getInt(LViewOffsets.ObjectMapNodeNetId).toLong()
			if (networkID <= 0 || abs(networkID - 0x40000000L) > 0x100000L)
				continue
			
			val unitAddress = data.getInt(LViewOffsets.ObjectMapNodeObject).toLong()
			if (unitAddress <= 0) continue
			
			unitAddresses[unitCount++] = unitAddress
		}
		
		return unitCount
	}
	
	private fun updateUnits(process: AttachedProcess, unitCount: Int) {
		val pointer = Pointer.alloc(0x4000)
		for (i in 0..unitCount - 1) {
			val unitAddress = unitAddresses[i]
			if (unitAddress <= 0) continue
			
			val networkID = process.int(unitAddress + GameObject.ObjNetworkID)
			if (networkID < 0) continue
			
			val unit: Unit
			if (units.containsKey(networkID)) {
				unit = units[networkID]
				if (unit.update(process, pointer, false) && networkID != unit.networkID)
					units.put(unit.networkID.toInt(), unit)
			} else {
				unit = Unit(unitAddress)
				if (unit.update(process, pointer, true))
					units.put(unit.networkID.toInt(), unit)
			}
			
			if (unit.isVisible)
				unit.lastVisibleAt = GameTime.gameTime
			
			if (unit.networkID > 0) {
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
		pointer.free(0x4000)
	}
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val objectManagerOffset = process.int(base.address + Offsets.ObjectManager).toLong()
		if (objectManagerOffset <= 0) return false
		
		val objectManager = Pointer.alloc(256)
		process.read(objectManagerOffset, objectManager, 256)
		if (!objectManager.readable()) return false
		
		try {
			val unitCount = scanUnits(process, objectManager)
			if (unitCount < 1) return false
			updateUnits(process, unitCount)
		} finally {
			objectManager.free(256)
		}
		
		return true
	}
	
}