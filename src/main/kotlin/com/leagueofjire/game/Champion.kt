package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.LViewOffsets
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

class Champion(address: Long) : Unit(address) {
	
	val spells = Array(6) { Spell(it) }
	val itemSlots = Array(6) { ItemSlot(it) }
	
	fun update(process: AttachedProcess, data: Pointer, deep: Boolean = false): Boolean {
		return updateSpells(process, data, deep) && updateItems(process)
	}
	
	private fun updateSpells(process: AttachedProcess, data: Pointer, deep: Boolean): Boolean {
		for (spell in spells) {
			val address = data.getInt(GameObject.ObjSpellBook + (spell.slot * 4)).toLong()
			if (address <= 0) return false
			if (!spell.load(process, address, deep)) return false
		}
		return true
	}
	
	private fun updateItems(process: AttachedProcess): Boolean {
		val itemsAddress = process.int(address + GameObject.ObjItemList).toLong()
		if (itemsAddress <= 0) return false
		
		val itemsData = process.readPointer(itemsAddress, 0x100)
		if (!itemsData.readable()) return false
		
		for (slot in itemSlots) {
			slot.isEmpty = true
			
			val itemPtr = itemsData.getInt((slot.slot * 0x10) + LViewOffsets.ItemListItem)
			if (itemPtr <= 0) continue
			
			val itemInfoPtr = process.int(itemPtr + LViewOffsets.ItemInfo).toLong()
			if (itemInfoPtr <= 0) continue
			
			val id = process.int(itemInfoPtr + LViewOffsets.ItemInfoId)
			slot.isEmpty = false
			// slot.stats = GetItemInfoById(id)
		}
		
		return true
	}
	
}