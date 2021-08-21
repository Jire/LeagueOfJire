package org.jire.leagueofjire

import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.model.Champion
import org.jire.leagueofjire.unit.Unit
import org.jire.leagueofjire.model.Renderer
import org.jire.leagueofjire.offsets.Offsets

object FindHoveredObject {
	
	fun find(lol: AttachedProcess, base: Long, renderer: Renderer, player: Champion) {
		val addrObj = lol.int(base + Offsets.UnderMouseObject).toLong()
		println("bruh $addrObj")
		if (addrObj <= 0) return
		
		println("HEY!! $addrObj")
/*		val netID = lol.int(addrObj + GameObject.ObjNetworkID)
		if (netID < 0) return
		println("netID: $netID")*/
		// grab using objectMap based off the netID...
		
		val ent = Unit(addrObj)
		ent.load(lol, true, renderer, player)
		println("yo ${ent.name} !! :)")
	}
	
}