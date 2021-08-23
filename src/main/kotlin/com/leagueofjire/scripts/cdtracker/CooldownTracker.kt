package com.leagueofjire.scripts.cdtracker

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.Spell
import com.leagueofjire.game.SpellInfo
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import com.leagueofjire.util.round

class CooldownTracker(
	val iconSize: Float = 28F,
	val readyDarkness: Float = 0.8F,
	val unreadyDarkness: Float = 0.4F,
	val textColor: Color = Color.WHITE
) : Script() {
	
	private val yOffset = iconSize * 2
	
	private val xTextOffset = -iconSize + (iconSize / 4) - 4
	private val yTextOffset = (iconSize / 2) + 2
	
	override fun ScriptContext.setup() {}
	
	override fun ScriptContext.run() {
		for (champion in unitManager.champions) champion.run {
			if (isVisible && isAlive) renderer.screen(this).use {
				val drawY = y + yOffset
				var xOffset = -yOffset
				
				for (spell in spells) {
					drawSpell(spell, x + xOffset, drawY)
					xOffset += iconSize
				}
			}
		}
	}
	
	private fun ScriptContext.drawSpell(spell: Spell, x: Float, y: Float) = with(Overlay) {
		val spellData = spell.info
		if (spellData === SpellInfo.unknownSpell) return
		val icon = spellData.loadIcon ?: return
		
		val levelled = spell.level >= 1
		val remaining = spell.readyAt - gameTime.gameTime
		val ready = remaining <= 0
		
		if (!levelled || !ready) sprites.setDarkness(unreadyDarkness)
		
		sprites.drawSprite(icon, x - iconSize, y, iconSize, iconSize)
		sprites.setDarkness(readyDarkness)
		
		if (levelled && !ready) {
			texts.color = textColor
			texts.text(remainingToString(remaining), x + xTextOffset, y + yTextOffset)
		}
	}
	
	private fun remainingToString(remaining: Float) =
		if (remaining < 1) remaining.round(1).toString()
		else remaining.toInt().toString()
	
}