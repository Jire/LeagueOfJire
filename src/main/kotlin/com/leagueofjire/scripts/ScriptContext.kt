package com.leagueofjire.scripts

import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.autosmite.AutoSmite
import com.leagueofjire.scripts.cdtracker.CooldownTracker
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import java.io.File

class ScriptContext(
	val overlay: Overlay,
	val gameTime: GameTime,
	val renderer: Renderer,
	val unitManager: UnitManager,
	val localPlayer: LocalPlayer,
	val hoveredUnit: HoveredUnit,
	val robot: Robot = Robot()
) {
	
	fun update() {
		val process = overlay.process
		val base = overlay.base
		gameTime.update(process, base)
		renderer.update(process, base)
		// minimap
		unitManager.update(process, base)
		// clear missing objects
		localPlayer.update(process, base)
		//HoveredObject.update(process, base)
		// get map, summoner's rift / howling etc.
	}
	
	private val unitHooks: ObjectList<UnitHook> = ObjectArrayList()
	
	fun unitHook(hook: UnitHook) {
		unitHooks.add(hook)
	}
	
	fun render() {
		overlay.batch.begin()
		
		for (entry in Int2ObjectMaps.fastIterable(unitManager.units)) {
			val unit = entry.value
			for (i in 0..unitHooks.lastIndex)
				unitHooks[i](unit)
		}
		
		overlay.batch.end()
	}
	
	private val scripts: ObjectList<Script> = ObjectArrayList()
	
	private fun loadScripts(manual: Boolean = true) {
		if (manual) {
			// these should be loaded from the files and/or classpath
			scripts.add(AutoSmite())
			scripts.add(CooldownTracker())
		} else {
			val files = File("scripts").listFiles()!!
			for (file in files) Overlay.evalFile(file)
		}
	}
	
	private fun setupScripts() {
		for (script in scripts) with(script) {
			setup()
		}
	}
	
	fun load() {
		loadScripts()
		setupScripts()
	}
	
	fun key(keycode: Int) {
		robot.keyPress(keycode)
		robot.keyRelease(keycode)
	}
	
	fun mouse(x: Int, y: Int) = robot.mouseMove(x, y)
	
	fun mouseLocation(): Point = MouseInfo.getPointerInfo().location
	
	inline fun mouse(x: Int, y: Int, beforeReset: () -> Unit) {
		val beforeMove = mouseLocation()
		mouse(x, y)
		beforeReset()
		mouse(beforeMove.x, beforeMove.y)
	}
	
	inline fun mouse(vector2D: Vector2D, beforeReset: () -> Unit) =
		mouse(vector2D.x.toInt(), vector2D.y.toInt(), beforeReset)
	
}