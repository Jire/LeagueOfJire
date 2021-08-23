package com.leagueofjire.scripts.cdtracker

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import kotlin.math.roundToInt

object CooldownTracker : Script() {
	
	private const val SCALE = 32F
	
	fun init() = Overlay {
		if (!isVisible || !isAlive || !info.isChampion || name.isEmpty()) return@Overlay
		
		val (x, y) = Renderer.worldToScreen(x, y, z)
		
		var xOffset = -SCALE * 2
		for (spell in spells) {
			drawSpell(spell, x + xOffset, y + (SCALE * 2))
			
			xOffset += SCALE
		}
	}
	
	private fun drawSpell(spell: Spell, x: Float, y: Float) = Overlay.run {
		val spellData = spell.info
		if (spellData === SpellInfo.unknownSpell) return@run
		val icon = spellData.loadIcon ?: return@run
		
		val levelled = spell.level >= 1
		val ready = GameTime.gameTime >= spell.readyAt
		
		val lit = 0.8F
		val dimmed = lit / 2
		if (!levelled || !ready) batch.setColor(dimmed, dimmed, dimmed, 1F) // dim
		
		batch.draw(icon, x - SCALE, y, SCALE, SCALE, 0, 0, 64, 64, false, true)
		batch.setColor(lit, lit, lit, 1F)
		
		if (levelled && !ready) {
			val remaining = spell.readyAt - GameTime.gameTime
			textRenderer.color = Color.WHITE
			textRenderer.draw(
				batch,
				remaining.roundToInt().toString(),
				x - SCALE + (SCALE / 4) - (SCALE / 10),
				y + (SCALE / 2) + (SCALE / 10)
			)
		}
	}
	
	override fun ScriptContext.run() {}
	
}