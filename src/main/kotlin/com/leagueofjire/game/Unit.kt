package com.leagueofjire.game

import com.leagueofjire.game.offsets.GameObject
import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.util.free
import org.jire.kna.Addressed
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import kotlin.math.abs

open class Unit(override val address: Long) : Addressed, Positioned {
	
	var disableUpdating = false
	
	var name = ""
	
	var lastVisibleAt = -1F
	
	var team = -1
	override var x = -1F
	override var y = -1F
	override var z = -1F
	var health = -1F
	var maxHealth = -1F
	var baseAttack = -1F
	var bonusAttack = -1F
	var armor = -1F
	var bonusArmor = -1F
	var magicResist = -1F
	var duration = -1F
	var isVisible = false
	var objectIndex = -1
	var crit = -1F
	var critMulti = -1F
	var abilityPower = -1F
	var attackSpeedMulti = -1F
	var movementSpeed = -1F
	var networkID = -1
	var spawnCount = -1
	var isAlive = false
	var attackRange = -1F
	
	var info: UnitInfo = UnitInfo.unknownInfo
	
	fun update(process: AttachedProcess, data: Pointer, deep: Boolean = false): Boolean {
		if (disableUpdating) return false
		if (!process.read(address, data, DATA_SIZE)) return false
		if (!data.readable()) return false
		
		data.run {
			team = getShort(GameObject.ObjTeam).toInt()
			x = getFloat(GameObject.ObjPos)
			y = getFloat(GameObject.ObjPos + 4)
			z = getFloat(GameObject.ObjPos + 8)
			health = getFloat(GameObject.HP)
			maxHealth = getFloat(GameObject.ObjMaxHealth)
			baseAttack = getFloat(GameObject.ObjBaseAtk)
			bonusAttack = getFloat(GameObject.ObjBonusAtk)
			armor = getFloat(GameObject.ObjArmor)
			bonusArmor = getFloat(GameObject.ObjBonusArmor)
			magicResist = getFloat(GameObject.ObjMagicRes)
			duration = getFloat(GameObject.ObjExpiry)
			isVisible = getBoolean(GameObject.ObjVisibility)
			objectIndex = getShort(GameObject.ObjIndex).toInt()
			crit = getFloat(GameObject.ObjCrit)
			critMulti = getFloat(GameObject.ObjCritMulti)
			abilityPower = getFloat(GameObject.ObjAbilityPower)
			attackSpeedMulti = getFloat(GameObject.ObjAtkSpeedMulti)
			movementSpeed = getFloat(GameObject.ObjMoveSpeed)
			networkID = getInt(GameObject.ObjNetworkID)
			attackRange = getFloat(GameObject.ObjAtkRange)
			
			spawnCount = getInt(GameObject.ObjSpawnCount)
			isAlive = spawnCount % 2 == 0
		}
		
		if (deep) deepUpdate(process, data)
		
		if (info.isChampion) {
			updateChampion(process, data, deep)
		} else if (info == UnitInfo.unknownInfo) {
			// try reading missile extension
		}
		
		return true
	}
	
	private fun deepUpdate(process: AttachedProcess, data: Pointer): Boolean {
		val nameAddress = data.getInt(GameObject.ObjName).toLong()
		if (nameAddress <= 0) return false
		
		name = RiotStrings().riotString(process, nameAddress)
		if (name.isNotEmpty()) {
			info = UnitInfo.nameToInfo[name] ?: UnitInfo.unknownInfo
			
			// "trash objects"
			when (name) {
				"testcube", "testcuberender", "testcuberender10vision",
				"s5test_wardcorpse",
				"sru_camprespawnmarker", "sru_plantrespawnmarker",
				"preseason_turret_shield" -> disableUpdating = true
			}
		}
		
		return true
	}
	
	var spells: Array<Spell> = defaultSpells
	var itemSlots: Array<ItemSlot> = defaultItemSlots
	
	fun updateChampion(process: AttachedProcess, data: Pointer, deep: Boolean = false) =
		updateSpells(process, data, deep) && updateItems(process)
	
	private fun updateSpells(process: AttachedProcess, data: Pointer, deep: Boolean): Boolean {
		if (spells === defaultSpells) spells = Array(6) { Spell(it) }
		for (spell in spells) {
			val address = data.getInt(GameObject.ObjSpellBook + (spell.slot * 4)).toLong()
			if (address <= 0) return false
			if (!spell.load(process, address, deep)) return false
		}
		return true
	}
	
	private fun updateItems(process: AttachedProcess): Boolean {
		if (true) return true
		
		val itemsAddress = process.int(address + GameObject.ObjItemList).toLong()
		if (itemsAddress <= 0) return false
		
		val itemsData = Pointer.alloc(0x100)
		if (!process.read(itemsAddress, itemsData, 0x100)) return false
		if (!itemsData.readable()) return false
		
		try {
			if (itemSlots === defaultItemSlots) itemSlots = Array(6) { ItemSlot(it) }
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
		} finally {
			itemsData.free(0x100)
		}
	}
	
	fun distance(otherUnit: Unit) = abs(x - otherUnit.x) + abs(y - otherUnit.y) + abs(z - otherUnit.z)
	
	fun withinDistance(otherUnit: Unit, distance: Float) = distance(otherUnit) <= distance
	
	companion object {
		
		private const val DATA_SIZE = 0x4000L
		
		private val defaultSpells = emptyArray<Spell>()
		private val defaultItemSlots = emptyArray<ItemSlot>()
		
	}
	
}