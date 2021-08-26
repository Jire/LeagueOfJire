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

import com.leagueofjire.core.LeagueOfLegendsHook

class DefaultGameContext(
	override val hook: LeagueOfLegendsHook,
	override val time: IGameTime,
	override val renderer: IGameRenderer,
	override val minimap: IGameMinimap,
	override val unitManager: IGameUnitManager,
	override val me: IGameLocalPlayer,
	override val hoveredUnit: IGameHoveredUnit
) : IGameContext {
	
	override fun update() = hook.run {
		time.update(process, baseModule)
				&& renderer.update(process, baseModule)
				&& minimap.update(process, baseModule)
				&& unitManager.update(process, baseModule)
				// clear missing objects
				&& me.update(process, baseModule)
				// && hoveredUnit.update(process, base)
				// get map, summoner's rift / howling etc.
	}
	
}