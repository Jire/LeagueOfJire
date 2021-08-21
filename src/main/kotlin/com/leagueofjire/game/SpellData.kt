package com.leagueofjire.game

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import javax.imageio.ImageIO

data class SpellData(
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
	val hasIcon = icon.isNotBlank()
	val loadIcon by lazy(LazyThreadSafetyMode.NONE) {
		Texture(Pixmap(Gdx2DPixmap(SpellData::class.java.getResourceAsStream("icons_spells/${icon.lowercase()}.png"), Gdx2DPixmap.GDX2D_FORMAT_RGBA4444)))
	}
	
	companion object {
		
		val objectMapper = ObjectMapper().registerKotlinModule()
		
		val nameToData: Object2ObjectMap<String, SpellData> = Object2ObjectOpenHashMap()
		
		fun load() {
			loadFile("spells.json")
			loadFile("summoner_spells.json")
		}
		
		fun loadFile(file: String) {
			val data: List<SpellData> = objectMapper.readValue(
				SpellData::class.java.getResource(file),
				objectMapper.typeFactory.constructCollectionType(List::class.java, SpellData::class.java)
			)
			nameToData
			data.forEach { nameToData[it.name.lowercase()] = it }
		}
		
	}
	
}