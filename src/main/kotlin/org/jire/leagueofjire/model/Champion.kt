package org.jire.leagueofjire.model

import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.offsets.GameObject
import org.jire.leagueofjire.offsets.LViewOffsets
import org.jire.leagueofjire.unit.Unit

class Champion(address: Long) : Unit(address) {
	
	val spells = Array(6) { Spell(it) }
	val itemSlots = Array(6) { ItemSlot(it) }
	
	fun loadChampion(buff: Pointer, lol: AttachedProcess, deepLoad: Boolean): Boolean {
		for (spell in spells) {
			val address = buff.getInt(GameObject.ObjSpellBook + (spell.slot * 4)).toLong()
			if (address <= 0) continue
			spell.load(lol, deepLoad, address)
		}
		
		// items
		val itemsAddress = lol.int(address + GameObject.ObjItemList).toLong()
		if (itemsAddress <= 0) return false
		val itemsData = lol.readPointer(itemsAddress, 0x100)
		if (!itemsData.readable()) return false
		for (slot in itemSlots) {
			slot.isEmpty = true
			
			val itemPtr = itemsData.getInt((slot.slot * 0x10) + LViewOffsets.ItemListItem)
			if (itemPtr <= 0) continue
			
			val itemInfoPtr = lol.int(itemPtr + LViewOffsets.ItemInfo).toLong()
			if (itemInfoPtr <= 0) continue
			
			val id = lol.int(itemInfoPtr + LViewOffsets.ItemInfoId)
			slot.isEmpty = false
			// slot.stats = GetItemInfoById(id)
		}
		
		return true
	}
	
}