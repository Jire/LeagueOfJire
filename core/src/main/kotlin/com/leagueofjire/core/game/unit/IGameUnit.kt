package com.leagueofjire.core.game.unit

import com.leagueofjire.core.game.RiotStrings
import com.leagueofjire.core.game.unit.champion.item.ItemSlot
import com.leagueofjire.core.game.unit.champion.spell.IGameChampionSpell
import com.leagueofjire.core.offsets.GameObject
import com.leagueofjire.core.offsets.LViewOffsets
import com.leagueofjire.game.unit.GameUnit
import com.leagueofjire.core.util.free
import com.leagueofjire.game.unit.champion.GameChampion
import com.leagueofjire.game.unit.champion.spell.GameChampionSpell
import com.leagueofjire.game.unit.champion.spell.GameChampionSpells
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import kotlin.math.abs

open class IGameUnit : GameUnit, GameChampion {
	
	override fun spell(spell: GameChampionSpells): GameChampionSpell? {
		for (iSpell in spells)
			if (iSpell.type.api == spell)
				return iSpell
		return null
	}
	
	override var address = -1L
	
	var disableUpdating = false
	
	override val position = IGamePosition()
	
	override var name = ""
	
	override var lastVisibleTime = -1F
	
	override var team = -1
	override var health = -1F
	override var maxHealth = -1F
	override var baseAttack = -1F
	override var bonusAttack = -1F
	override var armor = -1F
	override var bonusArmor = -1F
	override var magicResist = -1F
	override var duration = -1F
	override var isVisible = false
	override var objectIndex = -1
	override var crit = -1F
	override var critMulti = -1F
	override var abilityPower = -1F
	override var attackSpeedMulti = -1F
	override var movementSpeed = -1F
	override var networkID = -1
	override var spawnCount = -1
	override var isAlive = false
	override var attackRange = -1F
	
	var info: UnitInfo = UnitInfo.unknownInfo
	
	fun update(process: AttachedProcess, data: Pointer, deep: Boolean = false): Boolean {
		if (disableUpdating) return false
		if (!process.read(address, data, DATA_SIZE)) return false
		if (!data.readable()) return false
		
		data.run {
			team = getShort(GameObject.ObjTeam).toInt()
			position.x = getFloat(GameObject.ObjPos)
			position.y = getFloat(GameObject.ObjPos + 4)
			position.z = getFloat(GameObject.ObjPos + 8)
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
	
	var spells: Array<IGameChampionSpell> = defaultSpells
	var itemSlots: Array<ItemSlot> = defaultItemSlots
	
	fun updateChampion(process: AttachedProcess, data: Pointer, deep: Boolean = false) =
		updateSpells(process, data, deep) && updateItems(process)
	
	private fun updateSpells(process: AttachedProcess, data: Pointer, deep: Boolean): Boolean {
		if (spells === defaultSpells) spells = Array(6) { IGameChampionSpell(it) }
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
	
	fun distance(otherUnit: IGameUnit) =
		abs(position.x - otherUnit.position.x) +
				abs(position.y - otherUnit.position.y) +
				abs(position.z - otherUnit.position.z)
	
	fun withinDistance(otherUnit: IGameUnit, distance: Float) = distance(otherUnit) <= distance
	
	companion object {
		
		private const val DATA_SIZE = 0x4000L
		
		private val defaultSpells = emptyArray<IGameChampionSpell>()
		private val defaultItemSlots = emptyArray<ItemSlot>()
		
	}
	
}