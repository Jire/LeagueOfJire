package com.leagueofjire.game.unit.champion

import com.leagueofjire.game.unit.GameUnit

inline fun GameUnit.asChampion(useChampion: GameChampion.() -> Unit): GameChampion? {
	if (!isChampion) return null
	
	val champion = this as GameChampion
	useChampion(champion)
	return champion
}