package com.leagueofjire.game

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

enum class SummonerSpellType(val typeName: String) {
	
	NONE(""),
	GHOST("summonerhaste"),
	HEAL("summonerheal"),
	BARRIER("summonerbarrier"),
	EXHAUST("summonerexhaust"),
	CLARITY("summonermana"),
	SNOWBALL("summonermark"),
	FLASH("summonerflash"),
	TELEPORT("summonerteleport"),
	CLEANSE("summonerboost"),
	IGNITE("summonerdot"),
	SMITE("summonersmite");
	
	companion object {
		
		val values = values()
		
		val typeToSpell: Object2ObjectMap<String, SummonerSpellType> by lazy(LazyThreadSafetyMode.NONE) {
			val map: Object2ObjectMap<String, SummonerSpellType> = Object2ObjectOpenHashMap(values.size)
			for (value in values) map[value.typeName] = value
			map["s5_summonersmiteplayerganker"] = SMITE
			map["s5_summonersmiteduel"] = SMITE
			map
		}
		
	}
	
}