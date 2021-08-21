package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.game.Renderer
import com.leagueofjire.game.UnitManager
import com.leagueofjire.input.Keyboard
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.overlay.Screen
import java.awt.event.KeyEvent

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
			textRenderer.run {
				batch.begin()
				for (unit in UnitManager.objectMap) {
					unit ?: continue
					unit.run {
						if (isVisible && isAlive && name.isNotEmpty()) {
							val w2s = Renderer.worldToScreen(x, y, z)
							textRenderer.color = Color.WHITE
							textRenderer.draw(batch, name, w2s.first, w2s.second)
							textRenderer.color = Color.YELLOW
							textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
							
							textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
						}
					}
				}
				
				batch.end()
				
			}
		}
	}
	
}