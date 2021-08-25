package com.leagueofjire.game.unit

import com.leagueofjire.game.GamePosition

interface GameUnit {
	
	val address: Long
	
	val position: GamePosition
	
	val name: String
	val lastVisibleTime: Float
	val team: Int
	val health: Float
	val maxHealth: Float
	val baseAttack: Float
	val bonusAttack: Float
	val armor: Float
	val bonusArmor: Float
	val magicResist: Float
	val duration: Float
	val isVisible: Boolean
	val objectIndex: Int
	val crit: Float
	val critMulti: Float
	val abilityPower: Float
	val attackSpeedMulti: Float
	val movementSpeed: Float
	val networkID: Int
	val spawnCount: Int
	val isAlive: Boolean
	val attackRange: Float
	
	val isChampion: Boolean
	val isImportantJungle: Boolean
	
}