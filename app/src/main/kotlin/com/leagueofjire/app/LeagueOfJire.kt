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

package com.leagueofjire.app

import com.badlogic.gdx.utils.ScreenUtils
import com.leagueofjire.core.game.*
import com.leagueofjire.core.LeagueOfLegendsHook
import com.leagueofjire.core.game.unit.champion.spell.SpellInfo
import com.leagueofjire.core.game.unit.UnitInfo

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		val hook = LeagueOfLegendsHook.hook()
		
		UnitInfo.load()
		SpellInfo.load()
		
		val gameContext = DefaultGameContext(
			hook,
			IGameTime,
			IGameRenderer,
			IGameMinimap,
			IGameUnitManager,
			IGameLocalPlayer,
			IGameHoveredUnit
		)
		if (!gameContext.update()) return
		
		val overlay = Overlay(gameContext)
		val scriptManager = ScriptManager(gameContext, overlay)
		
		overlay.open {
			scriptManager.load()
			renderHook {
				if (gameContext.update()) {
					ScreenUtils.clear(0F, 0F, 0F, 0F)
					scriptManager.render()
				}
			}
		}
	}
	
}