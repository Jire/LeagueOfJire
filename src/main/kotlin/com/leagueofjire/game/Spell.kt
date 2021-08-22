package com.leagueofjire.game

import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

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
	var data: SpellInfo = SpellInfo.unknownSpell
	
	fun load(process: AttachedProcess, address: Long, data: Pointer, deep: Boolean = false): Boolean {
		this.address = address
		if (address <= 0) return false
		
		process.read(address, data, 0x150)
		if (!data.readable()) return false
		
		readyAt = data.getFloat(SpellSlotTime)
		level = data.getInt(SpellSlotLevel)
		value = data.getFloat(SpellSlotDamage)
		
		return !deep || deepLoad(process, data)
	}
	
	private fun deepLoad(process: AttachedProcess, data: Pointer): Boolean {
		val spellInfoPtr = data.getInt(SpellSlotSpellInfo).toLong()
		if (spellInfoPtr <= 0) return false
		
		val spellDataPtr = process.int(spellInfoPtr + SpellInfoSpellData).toLong()
		if (spellDataPtr <= 0) return false
		
		val spellNamePtr = process.int(spellDataPtr + SpellDataSpellName).toLong()
		if (spellNamePtr <= 0) return false
		
		name = RiotStrings().riotString(process, spellNamePtr)
		type = SummonerSpellType.typeToSpell[name] ?: SummonerSpellType.NONE
		this.data = SpellInfo.nameToData[name] ?: SpellInfo.unknownSpell
		return true
	}
	
}