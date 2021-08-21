package com.leagueofjire.game

import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.util.riotString

class Spell(val slot: Int) {
	
	val slotEnum = SpellSlot.values[slot]
	
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
	
	var name = ""
	
	var readyAt = -1F
	var level = -1
	var value = -1F
	
	var type: SummonerSpellType = SummonerSpellType.NONE
	var data: SpellData? = null
	
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
		
		val spellDataPtr = process.int(spellInfoPtr + SpellInfoSpellData).toLong()
		if (spellDataPtr <= 0) return false
		
		val spellNamePtr = process.int(spellDataPtr + SpellDataSpellName).toLong()
		if (spellNamePtr <= 0) return false
		
		name = process.riotString(spellNamePtr).lowercase()
		type = SummonerSpellType.typeToSpell[name] ?: SummonerSpellType.NONE
		data = SpellData.nameToData[name]
		
		return deepLoad(process, address)
	}
	
	private fun deepLoad(process: AttachedProcess, address: Long): Boolean {
		return true
	}
	
}