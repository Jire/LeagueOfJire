package com.leagueofjire.scripts.cdtracker

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.GameTime
import com.leagueofjire.game.Renderer
import com.leagueofjire.game.Spell
import com.leagueofjire.game.SpellInfo
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import com.leagueofjire.util.round

class CooldownTracker(
	val scale: Float = 32F,
	val readyDarkness: Float = 0.8F,
	val unreadyDarkness: Float = 0.4F
) : Script() {
	
	override fun ScriptContext.run() {
		Overlay {
			if (!isVisible || !isAlive || !info.isChampion || name.isEmpty()) return@Overlay
			
			val (sx, sy) = Renderer.worldToScreen(x, y, z)
			
			val yOffset = scale * 2
			val drawY = sy + yOffset
			var xOffset = -yOffset
			
			for (spell in spells) {
				drawSpell(spell, sx + xOffset, drawY)
				xOffset += scale
			}
		}
	}
	
	private fun drawSpell(spell: Spell, x: Float, y: Float) = Overlay.run {
		val spellData = spell.info
		if (spellData === SpellInfo.unknownSpell) return@run
		val icon = spellData.loadIcon ?: return@run
		
		val levelled = spell.level >= 1
		val remaining = spell.readyAt - GameTime.gameTime
		val ready = remaining <= 0
		
		if (!levelled || !ready) batch.setColor(unreadyDarkness, unreadyDarkness, unreadyDarkness, 1F)
		
		batch.draw(icon, x - scale, y, scale, scale, 0, 0, 64, 64, false, true)
		batch.setColor(readyDarkness, readyDarkness, readyDarkness, 1F)
		
		if (levelled && !ready) {
			textRenderer.color = Color.WHITE
			textRenderer.draw(
				batch,
				if (remaining < 1) remaining.round(1).toString() else remaining.toInt().toString(),
				x - scale + (scale / 4) - (scale / 10),
				y + (scale / 2) + (scale / 10)
			)
		}
	}
	
}