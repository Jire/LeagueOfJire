package com.leagueofjire.scripts.lastpositiontracker

import com.badlogic.gdx.graphics.Color
import com.leagueofjire.scripts.Script
import com.leagueofjire.scripts.ScriptContext
import kotlin.math.min

class LastPositionTracker(
	val onMinimap: Boolean = true,
	val onMinimapColor: Color = Color.WHITE,
	val onMinimapChars: Int = 3,
	
	val onWorld: Boolean = true,
	val onWorldColor: Color = Color.WHITE
) : Script() {
	
	override fun ScriptContext.setup() = overlay.run {
		unitHook {
			if (isVisible || !info.isChampion || name.isEmpty()) return@unitHook
			
			val timeMissing = (gameTime.gameTime - lastVisibleAt).toInt()
			
			if (onMinimap) minimap.screen(this).use {
				val shortName = name.substring(0, min(onMinimapChars, name.length))
				texts.color = onMinimapColor
				texts.text("$shortName $timeMissing", x, y)
			}
			
			if (onWorld) renderer.screen(this).use {
				texts.color = onWorldColor
				texts.text("$name $timeMissing", x, y)
			}
		}
	}
	
}