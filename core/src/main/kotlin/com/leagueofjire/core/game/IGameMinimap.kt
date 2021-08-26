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

import com.leagueofjire.ScreenPosition
import com.leagueofjire.core.game.unit.IGameUnit
import com.leagueofjire.core.offsets.Offsets
import com.leagueofjire.game.GameMinimap
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object IGameMinimap : GameMinimap {
	
	private val data = Pointer.alloc(0x80)
	
	override var x = -1F
	override var y = -1F
	
	override var width = -1F
	override var height = -1F
	override fun update(): Boolean {
		TODO("Not yet implemented")
	}
	
	override fun get(x: Float, y: Float, z: Float) = screen(x, y, z)
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val minimapObject = process.int(base.address + Offsets.MinimapObject).toLong()
		if (minimapObject <= 0) return false
		
		val minimapHUD = process.int(minimapObject + Offsets.MinimapObjectHud).toLong()
		if (minimapHUD <= 0) return false
		
		if (!process.read(minimapHUD, data, 0x80)) return false
		if (!data.readable()) return false
		
		x = data.getFloat(Offsets.MinimapHudPos)
		y = data.getFloat(Offsets.MinimapHudPos + 4)
		
		width = data.getFloat(Offsets.MinimapHudSize)
		height = data.getFloat(Offsets.MinimapHudSize + 4)
		
		return true
	}
	
	const val WORLD_SCALE = 15000F
	
	fun screen(x: Float, y: Float, z: Float): ScreenPosition {
		var rx = x / WORLD_SCALE
		var ry = z / WORLD_SCALE
		rx = IGameMinimap.x + rx * width
		ry = IGameMinimap.y + height - (ry * height)
		return ScreenPosition(rx, ry)
	}
	
	fun screen(unit: IGameUnit) = screen(unit.position.x, unit.position.y, unit.position.z)
	
	fun distanceToMinimap(dist: Float) = (dist / WORLD_SCALE) * width
	
}