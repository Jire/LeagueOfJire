package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.game.offsets.Offsets
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.*
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import kotlin.math.abs

object UnitManager {
	
	private const val MAX_OBJECTS = 500
	
	val entityAddresses = LongArray(MAX_OBJECTS)
	
	val nodesToVisit: LongPriorityQueue = LongArrayFIFOQueue(MAX_OBJECTS)
	val visitedNodes: LongSet = LongOpenHashSet(MAX_OBJECTS)
	
	val objectMap: Int2ObjectMap<Unit> = Int2ObjectMaps.synchronize(Int2ObjectOpenHashMap(MAX_OBJECTS))
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val objectManagerOffset = process.int(base.address + Offsets.ObjectManager).toLong()
		if (objectManagerOffset <= 0) return false
		//println(objectManagerOffset)
		
		val objectManager = process.readPointer(objectManagerOffset, 256)
		if (!objectManager.readable()) return false
		
		val numMissiles = objectManager.getInt(LViewOffsets.ObjectMapCount)
		if (numMissiles <= 0) return false
		
		val rootNode = objectManager.getInt(LViewOffsets.ObjectMapRoot).toLong()
		if (rootNode <= 0) return false
		//println("numMissiles: $numMissiles, rootNode: $rootNode")
		
		nodesToVisit.clear()
		visitedNodes.clear()
		
		nodesToVisit.enqueue(rootNode)
		
		var nrObj = 0
		var reads = 0
		while (reads < MAX_OBJECTS && !nodesToVisit.isEmpty) {
			val node = nodesToVisit.dequeueLong()
			if (node <= 0 || visitedNodes.contains(node)) continue
			
			reads++
			visitedNodes.add(node)
			
			val buff = process.readPointer(node, 0x30)
			if (!buff.readable()) continue
			val childNode1 = buff.getInt(0).toLong()
			val childNode2 = buff.getInt(4).toLong()
			val childNode3 = buff.getInt(8).toLong()
			//println("bro $childNode1 and $childNode2 and $childNode3")
			nodesToVisit.enqueue(childNode1)
			nodesToVisit.enqueue(childNode2)
			nodesToVisit.enqueue(childNode3)
			
			// Network ids of the objects we are interested in start from 0x40000000. We do this check for performance reasons.
			val netId = buff.getInt(LViewOffsets.ObjectMapNodeNetId).toLong()
			if (netId <= 0 || abs(netId - 0x40000000L) > 0x100000L)
				continue
			
			val entityAddress = buff.getInt(LViewOffsets.ObjectMapNodeObject).toLong()
			if (entityAddress <= 0) continue
			
			//println("pointerArray[$nrObj] = $addr")
			entityAddresses[nrObj++] = entityAddress
		}
		//println()
		
		for (i in 0..nrObj - 1) {
			val entityAddress = entityAddresses[i]
			if (entityAddress <= 0) continue
			
			//println("BRO!! $i is $ptr")
			val netId = process.int(entityAddress + GameObject.ObjNetworkID)
			
			val unit = Unit(entityAddress)
			objectMap[netId] = unit
			unit.update(process, true)
		}
		
		return true
	}
	
}