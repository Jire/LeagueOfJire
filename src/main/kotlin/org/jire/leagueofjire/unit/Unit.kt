package org.jire.leagueofjire.unit

import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedProcess
import org.jire.leagueofjire.model.Champion
import org.jire.leagueofjire.model.Renderer
import org.jire.leagueofjire.offsets.GameObject
import org.jire.leagueofjire.util.riotString
import java.awt.MouseInfo
import java.awt.Robot
import kotlin.math.abs

open class Unit(val address: Long) {
	
	companion object {
		const val sizeBuff = 0x4000L
		const val sizeBuffDeep = 0x1000L
	}
	
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
	
	fun load(lol: AttachedProcess, deepLoad: Boolean, renderer: Renderer, player: Champion): Pointer? {
		if (address <= 0) return null
		val buff = lol.readPointer(address, sizeBuff)
		if (!buff.readable()) return null
		
		team = buff.getShort(GameObject.ObjTeam).toInt()
		x = buff.getFloat(GameObject.ObjPos)
		y = buff.getFloat(GameObject.ObjPos + 4)
		z = buff.getFloat(GameObject.ObjPos + 8)
		health = buff.getFloat(GameObject.HP)
		maxHealth = buff.getFloat(GameObject.ObjMaxHealth)
		baseAttack = buff.getFloat(GameObject.ObjBaseAtk)
		bonusAttack = buff.getFloat(GameObject.ObjBonusAtk)
		armor = buff.getFloat(GameObject.ObjArmor)
		bonusArmor = buff.getFloat(GameObject.ObjBonusArmor)
		magicResist = buff.getFloat(GameObject.ObjMagicRes)
		duration = buff.getFloat(GameObject.ObjExpiry)
		isVisible = buff.getBoolean(GameObject.ObjVisibility)
		objectIndex = buff.getShort(GameObject.ObjIndex).toInt()
		crit = buff.getFloat(GameObject.ObjCrit)
		critMulti = buff.getFloat(GameObject.ObjCritMulti)
		abilityPower = buff.getFloat(GameObject.ObjAbilityPower)
		attackSpeedMulti = buff.getFloat(GameObject.ObjAtkSpeedMulti)
		movementSpeed = buff.getFloat(GameObject.ObjMoveSpeed)
		networkID = buff.getInt(GameObject.ObjNetworkID)
		
		spawnCount = buff.getInt(GameObject.ObjSpawnCount)
		isAlive = spawnCount % 2 == 0
		
		if (deepLoad) {
			val nameAddress = buff.getInt(GameObject.ObjName).toLong()
			if (nameAddress <= 0) return null
			
			name = lol.riotString(nameAddress)
			if (isVisible && isAlive && player.spells[5].value >= health
				&& abs(x - player.x) <= 500 && abs(y - player.y) <= 500 && abs(z - player.z) <= 500
				&& name.contains("crab", true)
			) {
				val robot = Robot()
				val w2s = renderer.worldToScreen(x, y, z)
				val beforeCoord = MouseInfo.getPointerInfo().location
				robot.mouseMove(w2s.first, w2s.second)
				robot.keyPress(70)
				Thread.sleep(1)
				robot.mouseMove(beforeCoord.x, beforeCoord.y)
				println("AUTOSMITED ${w2s.first},${w2s.second} name $name")
				//println(this)
				/*
				val champion = Champion(address)
				champion.loadChampion(buff, lol, deepLoad)
				 */
			}
			//println("\"$name\" pos: ($positionX,$positionY,$positionZ) team: $team health: $health, maxHealth: $maxHealth, baseAttack: $baseAttack, index: $objectIndex")
		}
		
		return buff
	}
	
	override fun toString(): String {
		return "Entity(address=$address, name='$name', team=$team, x=$x, y=$y, z=$z, health=$health, maxHealth=$maxHealth, baseAttack=$baseAttack, bonusAttack=$bonusAttack, armor=$armor, bonusArmor=$bonusArmor, magicResist=$magicResist, duration=$duration, isVisible=$isVisible, objectIndex=$objectIndex, crit=$crit, critMulti=$critMulti, abilityPower=$abilityPower, attackSpeedMulti=$attackSpeedMulti, movementSpeed=$movementSpeed, networkID=$networkID, spawnCount=$spawnCount, isAlive=$isAlive)"
	}
	
}