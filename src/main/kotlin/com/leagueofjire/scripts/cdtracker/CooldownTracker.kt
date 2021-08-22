package com.leagueofjire.scripts.cdtracker

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import kotlin.math.roundToInt

object CooldownTracker : Script() {
	
	fun init() = Overlay.run {
		Overlay {
			if (!isVisible || !isAlive || name.isEmpty()) return@Overlay
			
			val w2s = Renderer.worldToScreen(x, y, z - info.healthBarHeight)
			val x = w2s.x
			val y = w2s.y
			textRenderer.color = Color.WHITE
			textRenderer.draw(batch, name, x, y + 50)
			
			if (!info.isChampion) return@Overlay
			
			val scale = 32F
			var xOffset = scale * -2
			for (spell in spells) {
				drawSpell(spell, x + xOffset, y, scale)
				
				xOffset += scale
			}
		}
	}
	
	private fun drawSpell(spell: Spell, x: Float, y: Float, scale: Float) = Overlay.run {
		val spellData = spell.info
		if (spellData === SpellInfo.unknownSpell) return@run
		val icon = spellData.loadIcon ?: return@run
		
		val levelled = spell.level >= 1
		val ready = GameTime.gameTime >= spell.readyAt
		
		val lit = 0.8F
		val dimmed = lit / 2
		if (!levelled || !ready) batch.setColor(dimmed, dimmed, dimmed, 1F) // dim
		
		batch.draw(icon, x - scale, y, scale, scale, 0, 0, 64, 64, false, true)
		batch.setColor(lit, lit, lit, 1F)
		
		if (levelled && !ready) {
			val remaining = spell.readyAt - GameTime.gameTime
			textRenderer.color = Color.WHITE
			textRenderer.draw(
				batch,
				remaining.roundToInt().toString(),
				x - scale + (scale / 4) - (scale / 10),
				y + (scale / 2) + (scale / 10)
			)
		}
	}
	
	override fun ScriptContext.run() {}
	
}