package com.leagueofjire.scripts

import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot

class ScriptContext(
	val overlay: Overlay,
	val gameTime: GameTime,
	val renderer: Renderer,
	val minimap: Minimap,
	val unitManager: UnitManager,
	val localPlayer: LocalPlayer,
	val hoveredUnit: HoveredUnit,
	val robot: Robot = Robot().apply { autoDelay = 1; isAutoWaitForIdle = true }
) {
	
	fun update(): Boolean {
		val process = overlay.process
		val base = overlay.base
		return gameTime.update(process, base)
				&& renderer.update(process, base)
				&& minimap.update(process, base)
				&& unitManager.update(process, base)
				// clear missing objects
				&& localPlayer.update(process, base)
		// && hoveredUnit.update(process, base)
		// get map, summoner's rift / howling etc.
	}
	
	private val renderHooks: ObjectList<Overlay.() -> Unit> = ObjectArrayList()
	
	fun renderHook(hook: Overlay.() -> Unit) {
		renderHooks.add(hook)
	}
	
	private val unitHooks: ObjectList<UnitHook> = ObjectArrayList()
	
	fun unitHook(hook: UnitHook) {
		unitHooks.add(hook)
	}
	
	private val championHooks: ObjectList<UnitHook> = ObjectArrayList()
	
	fun championHook(hook: UnitHook) {
		championHooks.add(hook)
	}
	
	fun render() {
		overlay.sprites.begin()
		overlay.shapes.begin()
		
		for (i in 0..renderHooks.lastIndex) {
			renderHooks[i](overlay)
		}
		// should move the below hooks into renderhooks...
		for (i in 0..unitManager.champions.lastIndex) {
			val champion = unitManager.champions[i] ?: continue
			for (ci in 0..championHooks.lastIndex)
				championHooks[ci](champion)
		}
		for (entry in unitManager.unitsIt) {
			val unit = entry.value
			for (i in 0..unitHooks.lastIndex)
				unitHooks[i](unit)
		}
		
		overlay.sprites.end()
		overlay.shapes.end()
	}
	
	private val scripts: ObjectList<Script> = ObjectArrayList()
	
	private fun loadScripts() {
		ClassGraph().enableClassInfo()/*.acceptPackages("scripts")*/.scan().use { scanResult ->
			val scriptClassList = scanResult
				.getSubclasses(Script::class.java)
				.directOnly()
			println(scriptClassList.size)
			scriptClassList.forEach {
				val scriptClass = it.loadClass(Script::class.java)
				println(scriptClass.declaredConstructors.joinToString(","))
				val constructor = scriptClass.getDeclaredConstructor(
					ScriptContext::class.java,
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
					this@ScriptContext,
					overlay,
					gameTime,
					renderer,
					minimap,
					unitManager,
					localPlayer,
					hoveredUnit,
					robot
				)
				scripts.add(script)
			}
		}
	}
	
	fun load() {
		loadScripts()
	}
	
}