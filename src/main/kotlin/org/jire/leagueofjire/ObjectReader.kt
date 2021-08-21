package org.jire.leagueofjire

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongPriorityQueue
import it.unimi.dsi.fastutil.longs.LongSet
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.model.Champion
import org.jire.leagueofjire.model.Entity
import org.jire.leagueofjire.model.Renderer
import org.jire.leagueofjire.offsets.GameObject
import org.jire.leagueofjire.offsets.LViewOffsets
import org.jire.leagueofjire.offsets.Offsets
import kotlin.math.abs

object ObjectReader {
	
	private const val maxObjects = 500
	
	fun read(lol: AttachedProcess, baseAddress: Long, renderer: Renderer, player: Champion): Boolean {
		val objectManagerOffset = lol.int(baseAddress + Offsets.ObjectManager).toLong()
		if (objectManagerOffset <= 0) return false
		//println(objectManagerOffset)
		
		val objectManager = lol.readPointer(objectManagerOffset, 256)
		if (!objectManager.readable()) return false
		
		val numMissiles = objectManager.getInt(LViewOffsets.ObjectMapCount)
		if (numMissiles <= 0) return false
		val rootNode = objectManager.getInt(LViewOffsets.ObjectMapRoot).toLong()
		if (rootNode <= 0) return false
		//println("numMissiles: $numMissiles, rootNode: $rootNode")
		
		val nodesToVisit: LongPriorityQueue = LongArrayFIFOQueue().apply { enqueue(rootNode) }
		val visitedNodes: LongSet = LongOpenHashSet()
		
		val entityAddresses = LongArray(256)
		
		var nrObj = 0
		var reads = 0
		while (reads < maxObjects && !nodesToVisit.isEmpty) {
			val node = nodesToVisit.dequeueLong()
			if (node <= 0 || visitedNodes.contains(node)) continue
			
			reads++
			visitedNodes.add(node)
			
			val buff = lol.readPointer(node, 0x30)
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
			//val netId = lol.int(entityAddress + GameObject.ObjNetworkID)
			
			val entity = Entity(entityAddress)
			entity.load(lol, true, renderer, player)
			
			//objectMap[entity.networkId] = entity
		}
		
		return true
	}
	
}