package com.leagueofjire.game

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

data class SpellInfo(
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
	val hasIcon = icon.isNotEmpty()
	val loadIcon by lazy(LazyThreadSafetyMode.NONE) {
		if (!hasIcon) return@lazy null
		val resource = SpellInfo::class.java.getResourceAsStream("icons_spells/${icon.lowercase()}.png")
			?: return@lazy null
		Texture(Pixmap(Gdx2DPixmap(resource, Gdx2DPixmap.GDX2D_FORMAT_RGBA4444)))
	}
	
	companion object {
		
		val objectMapper = ObjectMapper().registerKotlinModule()
		
		val nameToData: Object2ObjectMap<String, SpellInfo> = Object2ObjectOpenHashMap()
		
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
		
		val unknownSpell = SpellInfo(
			"", "", 0, 0F, 0F,
			0F, 0F, 0F, 0F, 0F, false
		)
		
	}
	
}