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

package com.leagueofjire.core.game.unit.champion.spell

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

open class SpellInfo(
	val name: String,
	val icon: String,
	val flags: Int,
	val delay: Float,
	val castRange: Float,
	val castRadius: Float,
	val width: Float,
	val height: Float,
	val speed: Float,
	val travelTime: Float,
	val projectDestination: Boolean
) {
	open val hasIcon = icon.isNotEmpty()
	open val loadIcon by lazy(LazyThreadSafetyMode.NONE) {
		if (!hasIcon) return@lazy null
		val resource = SpellInfo::class.java.getResourceAsStream("icons_spells/${icon.lowercase()}.png")
			?: return@lazy null
		Texture(Pixmap(Gdx2DPixmap(resource, Gdx2DPixmap.GDX2D_FORMAT_RGBA4444)))
	}
	
	companion object {
		
		val objectMapper by lazy(LazyThreadSafetyMode.NONE) { ObjectMapper().registerKotlinModule() }
		
		val nameToData: Object2ObjectMap<String, SpellInfo> by lazy(LazyThreadSafetyMode.NONE) { Object2ObjectOpenHashMap() }
		
		fun load() {
			loadFile("spells.json")
			loadFile("summoner_spells.json")
		}
		
		private fun loadFile(file: String) {
			val data: List<SpellInfo> = objectMapper.readValue(
				SpellInfo::class.java.getResource(file),
				objectMapper.typeFactory.constructCollectionType(List::class.java, SpellInfo::class.java)
			)
			nameToData
			data.forEach { nameToData[it.name.lowercase()] = it }
		}
		
		val unknownSpell = object : SpellInfo(
			"", "", 0, 0F, 0F,
			0F, 0F, 0F, 0F, 0F, false
		) {
			override val hasIcon = false
			override val loadIcon: Texture? = null
		}
		
	}
	
}