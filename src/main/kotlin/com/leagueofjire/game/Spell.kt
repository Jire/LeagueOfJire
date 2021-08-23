package com.leagueofjire.game

import org.jire.kna.Pointer
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
		
		const val DATA_SIZE = 0x150L
	}
	
	val slotEnum = SpellSlot.values[slot]
	
	var address = -1L
	
	var name = ""
	
	var readyAt = -1F
	var level = -1
	var value = -1F
	
	fun canCast(gameTime: Float) = level > 0 && gameTime >= readyAt
	
	var type: SummonerSpellType = SummonerSpellType.NONE
	var info: SpellInfo = SpellInfo.unknownSpell
	
	private var namePointer = -1L
	
	private val data = Pointer.alloc(DATA_SIZE)
	
	fun load(process: AttachedProcess, address: Long, deep: Boolean = false): Boolean {
		this.address = address
		if (address <= 0) return false
		
		if (!process.read(address, data, DATA_SIZE)) return false
		if (!data.readable()) return false
		
		readyAt = data.getFloat(SpellSlotTime)
		level = data.getInt(SpellSlotLevel)
		value = data.getFloat(SpellSlotDamage)
		
		// always cuz champions like Nidalee that switch spells
		deepLoad(process, data)
		
		return true
	}
	
	private fun deepLoad(process: AttachedProcess, data: Pointer): Boolean {
		val infoPointer = data.getInt(SpellSlotSpellInfo).toLong()
		if (infoPointer <= 0) return false
		
		val dataPointer = process.int(infoPointer + SpellInfoSpellData).toLong()
		if (dataPointer <= 0) return false
		
		val namePointer = process.int(dataPointer + SpellDataSpellName).toLong()
		// optimization to only re-read if the name pointer has changed
		if (namePointer <= 0 || this.namePointer == namePointer) return false
		
		this.namePointer = namePointer
		name = RiotStrings().riotString(process, namePointer)
		type = SummonerSpellType.typeToSpell[name] ?: SummonerSpellType.NONE
		this.info = SpellInfo.nameToData[name] ?: SpellInfo.unknownSpell
		
		return true
	}
	
}