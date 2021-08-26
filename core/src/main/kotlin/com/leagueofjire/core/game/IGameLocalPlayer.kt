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

package com.leagueofjire.core.game

import com.leagueofjire.core.game.unit.IGameUnit
import com.leagueofjire.core.offsets.Offsets
import com.leagueofjire.game.unit.champion.GameChampionMe
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object IGameLocalPlayer : GameChampionMe, IGameUnit() {
	
	lateinit var localPlayer: IGameUnit
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val address = process.int(base.address + Offsets.LocalPlayer).toLong()
		if (address <= 0) return false
		
		val networkID = process.int(address + Offsets.ObjNetworkID)
		if (networkID <= 0) return false
		
		val unit = IGameUnitManager.units[networkID] ?: return false
		localPlayer = unit
		
		return true
	}
	
}