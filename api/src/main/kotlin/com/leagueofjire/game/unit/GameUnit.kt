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