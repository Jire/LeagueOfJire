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

package com.leagueofjire.core.game.unit.champion.item

import com.leagueofjire.game.unit.champion.item.GameChampionItem

class ItemSlot(val slot: Int) : GameChampionItem {
	
	var id = 0
	var cost = -1F
	var movementSpeed = -1F
	var health = -1F
	var crit = -1F
	var abilityPower = -1F
	var mana = -1F
	var armor = -1F
	var magicResist = -1F
	var physicalDamage = -1F
	var attackSpeed = -1F
	var lifeSteal = -1F
	var hpRegen = -1F
	var movementSpeedPercent = -1F
	var isEmpty = true
	
}