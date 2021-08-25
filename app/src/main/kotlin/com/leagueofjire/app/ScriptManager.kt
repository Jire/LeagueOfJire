package com.leagueofjire.app

import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.Script
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.Robot

object ScriptManager {
	
	private val scripts: ObjectList<Script> = ObjectArrayList()
	
	fun render() {
		Overlay.sprites.begin()
		Overlay.shapes.begin()
		
		for (si in 0..scripts.lastIndex) scripts[si].render()
		
		Overlay.sprites.end()
		Overlay.shapes.end()
	}
	
	private fun loadScripts() {
		ClassGraph().enableClassInfo()/*.acceptPackages("scripts")*/.scan().use { scanResult ->
			val scriptClassList = scanResult
				.getSubclasses(Script::class.java)
				.directOnly()
			scriptClassList.forEach {
				val scriptClass = it.loadClass(Script::class.java)
				val constructor = scriptClass.getDeclaredConstructor(
					Overlay::class.java,
					GameTime::class.java,
					Renderer::class.java,
					Minimap::class.java,
					UnitManager::class.java,
					LocalPlayer::class.java,
					HoveredUnit::class.java,
					Robot::class.java
				)
				val script = constructor.newInstance(
					Overlay,
					GameTime,
					Renderer,
					Minimap,
					UnitManager,
					LocalPlayer,
					HoveredUnit,
					Robot().apply { autoDelay = 1; isAutoWaitForIdle = true }
				)
				scripts.add(script)
			}
		}
	}
	
	fun load() {
		loadScripts()
		Overlay.renderHook { render() }
	}
	
}