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
	
	override fun ScriptContext.setup() = unitHook {
		if (!isVisible || !isAlive || !info.isChampion || name.isEmpty()) return@unitHook
		
		val (sx, sy) = renderer.worldToScreen(x, y, z)
		
		val drawY = sy + yOffset
		var xOffset = -yOffset
		
		for (spell in spells) {
			drawSpell(spell, sx + xOffset, drawY)
			xOffset += iconSize
		}
	}
	
	private fun ScriptContext.drawSpell(spell: Spell, x: Float, y: Float) = with(Overlay) {
		val spellData = spell.info
		if (spellData === SpellInfo.unknownSpell) return
		val icon = spellData.loadIcon ?: return
		
		val levelled = spell.level >= 1
		val remaining = spell.readyAt - gameTime.gameTime
		val ready = remaining <= 0
		
		if (!levelled || !ready) batch.setColor(unreadyDarkness, unreadyDarkness, unreadyDarkness, 1F)
		
		batch.draw(icon, x - iconSize, y, iconSize, iconSize, 0, 0, 64, 64, false, true)
		batch.setColor(readyDarkness, readyDarkness, readyDarkness, 1F)
		
		if (levelled && !ready) {
			textRenderer.color = textColor
			textRenderer.draw(batch, remainingToString(remaining), x + xTextOffset, y + yTextOffset)
		}
	}
	
	private fun remainingToString(remaining: Float) =
		if (remaining < 1) remaining.round(1).toString() else remaining.toInt().toString()
	
}