package com.leagueofjire.game.unit.champion

import com.leagueofjire.game.unit.GameUnit
import com.leagueofjire.game.unit.champion.spell.GameChampionSpell
import com.leagueofjire.game.unit.champion.spell.GameChampionSpells

interface GameChampion : GameUnit {
	
	fun spell(spell: GameChampionSpells): GameChampionSpell?
	
}