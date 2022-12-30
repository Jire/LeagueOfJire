/*
 * LeagueOfJire: Free & Open-Source External Scripting Platform
 * Copyright (C) 2021  Thomas G. P. Nappo (Jire)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.leagueofjire.core.game.unit.champion.spell

import com.badlogic.gdx.graphics.Texture
import com.leagueofjire.core.game.RiotStrings
import com.leagueofjire.core.offsets.Offsets.SpellDataSpellName
import com.leagueofjire.core.offsets.Offsets.SpellInfoSpellData
import com.leagueofjire.core.offsets.Offsets.SpellSlotDamage
import com.leagueofjire.core.offsets.Offsets.SpellSlotLevel
import com.leagueofjire.core.offsets.Offsets.SpellSlotSpellInfo
import com.leagueofjire.core.offsets.Offsets.SpellSlotTime
import com.leagueofjire.game.GameTime
import com.leagueofjire.game.unit.champion.spell.GameChampionSpell
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

class IGameChampionSpell(val slot: Int) : GameChampionSpell {
	
	companion object {
		const val DATA_SIZE = 0x150L
	}
	
	val slotEnum = SpellSlot.values[slot]
	
	var address = -1L
	
	var name = ""
	
	override var readyAtSeconds = -1F
	override var level = -1
	override var value = -1F
	override fun canCast(time: GameTime) = level > 0 && time.seconds >= readyAtSeconds
	override val sprite: Texture? get() = info.loadIcon
	
	var type: SummonerSpellType = SummonerSpellType.NONE
	var info: SpellInfo = SpellInfo.unknownSpell
	
	private var namePointer = -1L
	
	private val data = Pointer.alloc(DATA_SIZE)
	
	fun load(process: AttachedProcess, address: Long, deep: Boolean = false): Boolean {
		this.address = address
		if (address <= 0) return false
		
		if (!process.read(address, data, DATA_SIZE)) return false
		if (!data.readable()) return false
		
		readyAtSeconds = data.getFloat(SpellSlotTime)
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
