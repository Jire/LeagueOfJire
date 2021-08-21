package com.leagueofjire.game

import com.leagueofjire.game.RiotStrings.riotString
import com.leagueofjire.game.offsets.GameObject
import org.jire.kna.Addressed
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess

open class Unit(override val address: Long) : Addressed {
	
	var name = ""
	
	var team = -1
	var x = -1F
	var y = -1F
	var z = -1F
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
	
	var data: UnitData? = null
	
	fun update(process: AttachedProcess, deep: Boolean = false): Boolean {
		val data = process.readPointer(address, DATA_SIZE).apply {
			if (!readable()) return false
			
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
		
		return !deep || deepUpdate(process, data)
	}
	
	private fun deepUpdate(process: AttachedProcess, data: Pointer): Boolean {
		val nameAddress = data.getInt(GameObject.ObjName).toLong()
		if (nameAddress <= 0) return false
		
		name = process.riotString(nameAddress).lowercase()
		if (name.isNotEmpty()) {
			val jsonData = UnitData.nameToData[name]
			this.data = jsonData
			if (jsonData != null && jsonData.isChampion && this !is Champion /* prevent infinite loop */) {
				transformToChampion(process, data)
			}
		}
		
		return true
	}
	
	fun transformToChampion(process: AttachedProcess, data: Pointer = process.readPointer(address, DATA_SIZE)) =
		Champion(address).apply {
			name = this@Unit.name
			this.data = this@Unit.data
			update(process, false)
			updateChampion(process, data, true)
			UnitManager.objectMap[networkID] = this
		}
	
	companion object {
		
		private const val DATA_SIZE = 0x4000L
		
	}
	
}