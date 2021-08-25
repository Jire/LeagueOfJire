package com.leagueofjire.game.unit.champion.spell

import com.badlogic.gdx.graphics.Texture
import com.leagueofjire.game.GameTime

interface GameChampionSpell {
	
	val value: Float
	val level: Int
	val readyAtSeconds: Float
	
	fun canCast(time: GameTime): Boolean
	
	val sprite: Texture?
	
}