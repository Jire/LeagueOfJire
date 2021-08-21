package org.jire.leagueofjire.model

import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

class Spell(val slot: Int) {
	
	companion object {
		const val SpellSlotLevel = 0x20L
		const val SpellSlotTime = 0x28L
		const val SpellSlotDamage = 0x94L
		const val SpellSlotSpellInfo = 0x13CL
		const val SpellInfoSpellData = 0x44L
		const val SpellDataSpellName = 0x6CL
		const val SpellDataMissileName = 0x6CL
	}
	
	var addressSlot = -1L
	
	var readyAt = -1F
	var level = -1
	var value = -1F
	
	fun load(lol: AttachedProcess, deepLoad: Boolean, base: Long): Boolean {
		addressSlot = base
		if (addressSlot <= 0) return false
		
		val buffer = lol.readPointer(addressSlot, 0x150)
		if (!buffer.readable()) return false
		
		readyAt = buffer.getFloat(SpellSlotTime)
		level = buffer.getInt(SpellSlotLevel)
		value = buffer.getFloat(SpellSlotDamage)
		
		//println("slot $slot, ready=$readyAt, level=$level, value=$value")
		
		val spellInfoPtr = buffer.getInt(SpellSlotSpellInfo).toLong()
		if (spellInfoPtr <= 0) return false
		
		val spellDataPtr = lol.int(spellInfoPtr + SpellInfoSpellData)
		if (spellDataPtr <= 0) return false
		val spellNamePtr = lol.int(spellDataPtr + SpellDataSpellName)
		
		
		return true
	}
	
}