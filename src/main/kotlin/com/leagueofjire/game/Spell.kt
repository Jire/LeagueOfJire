package com.leagueofjire.game

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
	
	var address = -1L
	
	var readyAt = -1F
	var level = -1
	var value = -1F
	
	fun load(process: AttachedProcess, address: Long, deep: Boolean = false): Boolean {
		this.address = address
		if (address <= 0) return false
		
		val buffer = process.readPointer(address, 0x150)
		if (!buffer.readable()) return false
		
		readyAt = buffer.getFloat(SpellSlotTime)
		level = buffer.getInt(SpellSlotLevel)
		value = buffer.getFloat(SpellSlotDamage)
		
		//println("slot $slot, ready=$readyAt, level=$level, value=$value")
		
		val spellInfoPtr = buffer.getInt(SpellSlotSpellInfo).toLong()
		if (spellInfoPtr <= 0) return false
		
		val spellDataPtr = process.int(spellInfoPtr + SpellInfoSpellData)
		if (spellDataPtr <= 0) return false
		val spellNamePtr = process.int(spellDataPtr + SpellDataSpellName)
		
		return deepLoad(process, address)
	}
	
	private fun deepLoad(process: AttachedProcess, address: Long): Boolean {
		return true
	}
	
}