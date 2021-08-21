package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.Champion
import com.leagueofjire.game.GameTime
import com.leagueofjire.game.Renderer
import com.leagueofjire.game.UnitManager
import com.leagueofjire.input.Keyboard
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.overlay.Screen
import java.awt.event.KeyEvent
import javax.swing.Spring.height
import kotlin.math.roundToInt


object ScriptManager {
	
	@Volatile
	private var ran = false
	
	fun run(): Boolean {
		if (ran) return true
		ran = true
		Overlay {
			val x = Screen.WIDTH * 0.7F
			val y = Screen.HEIGHT - 25F
			textRenderer.run {
				batch.begin()
				
				color = Color.CHARTREUSE
				draw(batch, "LeagueOfJire", x, y)
				color = Color.GREEN
				draw(batch, "v0.1.0", x + 94, y)
				
				batch.end()
			}
		}
		autoSmite()
		entityNames()
		return true
	}
	
	private fun autoSmite() {
		Overlay {
			textRenderer.run {
				batch.begin()
				
				color = Color.MAGENTA
				val holding = Keyboard.keyPressed(KeyEvent.VK_F)
				draw(batch, "Autosmite?", 20F, 500F)
				color = if (holding) Color.GREEN else Color.RED
				draw(batch, if (holding) "ON" else "OFF", 98F, 500F)
				
				batch.end()
			}
		}
	}
	
	private fun entityNames() {
		Overlay {
			//val doAfter = ArrayList<() -> Unit>(128)
			textRenderer.run {
				batch.begin()
				for ((networkID, unit) in UnitManager.objectMap) {
					unit ?: continue
					unit.run {
						if (isVisible && isAlive && this is Champion && name.isNotEmpty()) {
							val data = this.data
							if (data != null) {
								//val w2s = Renderer.worldToScreen(x, y, z)
								//textRenderer.color = Color.WHITE
								//textRenderer.draw(batch, data.name, w2s.first, w2s.second)
								//textRenderer.color = Color.YELLOW
								//textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
								val scale = 32F
								var xOff = scale * -2
								for (spell in this.spells) {
									val spellData = spell.data ?: continue
									val w2s = Renderer.worldToScreen(x, y, z - data.healthBarHeight)
									val tx = w2s.first + xOff
									val ty = w2s.second
									val ready = GameTime.gameTime >= spell.readyAt
									val lit = 0.8F
									val dimmed = lit / 2
									if (!ready) batch.setColor(dimmed, dimmed, dimmed, 1F) // dim
									batch.draw(spellData.loadIcon, tx - scale, ty, scale, scale, 0, 0, 64, 64, false, true)
									batch.setColor(lit, lit, lit, 1F)
									if (!ready) {
										val remaining = spell.readyAt - GameTime.gameTime
										textRenderer.color = Color.WHITE
										textRenderer.draw(batch, "${remaining.roundToInt()}", tx - scale + (scale / 4) - (scale / 10), ty + (scale / 2) + (scale / 10))
									}
									xOff += scale
								}
								//textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
								
								/*doAfter.add {
									shapeRenderer.run {
										shapeRenderer.circle(w2s.first, w2s.second, attackRange)
									}
								}*/
							}
						}
					}
				}
				batch.end()
				
				/*if (doAfter.isNotEmpty()) {
					shapeRenderer.begin()
					for (i in 0..doAfter.lastIndex) doAfter[i]()
					shapeRenderer.end()
				}*/
			}
		}
	}
	
}