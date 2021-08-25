package com.leagueofjire.core.game.unit.champion.spell

import com.leagueofjire.game.unit.champion.spell.GameChampionSpells
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

enum class SummonerSpellType(val typeName: String, val api: GameChampionSpells?) {
	
	NONE("", null),
	GHOST("summonerhaste", GameChampionSpells.GHOST),
	HEAL("summonerheal", GameChampionSpells.HEAL),
	BARRIER("summonerbarrier", GameChampionSpells.BARRIER),
	EXHAUST("summonerexhaust", GameChampionSpells.EXHAUST),
	CLARITY("summonermana", GameChampionSpells.CLARITY),
	SNOWBALL("summonermark", GameChampionSpells.SNOWBALL),
	FLASH("summonerflash", GameChampionSpells.FLASH),
	TELEPORT("summonerteleport", GameChampionSpells.TELEPORT),
	CLEANSE("summonerboost", GameChampionSpells.CLEANSE),
	IGNITE("summonerdot", GameChampionSpells.IGNITE),
	SMITE("summonersmite", GameChampionSpells.SMITE);
	
	companion object {
		
		val values = values()
		
		val typeToSpell: Object2ObjectMap<String, SummonerSpellType> by lazy(LazyThreadSafetyMode.NONE) {
			val map: Object2ObjectMap<String, SummonerSpellType> = Object2ObjectOpenHashMap(values.size)
			for (value in values) map[value.typeName] = value
			map["s5_summonersmiteplayerganker"] = SMITE
			map["s5_summonersmiteduel"] = SMITE
			map
		}
		
		val apiToType: Object2ObjectMap<GameChampionSpells, SummonerSpellType> by lazy(LazyThreadSafetyMode.NONE) {
			val map: Object2ObjectMap<GameChampionSpells, SummonerSpellType> = Object2ObjectOpenHashMap(GameChampionSpells.values().size)
			for (value in values) map[value.api ?: continue] = value
			map
		}
		
	}
	
}