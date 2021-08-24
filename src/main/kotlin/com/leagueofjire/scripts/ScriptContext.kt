package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.scripts.autosmite.AutoSmite
import com.leagueofjire.scripts.cdtracker.CooldownTracker
import com.leagueofjire.scripts.lastpositiontracker.LastPositionTracker
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
		
		for (i in 0..scripts.lastIndex) {
			val script = scripts[i]
			with(script) {
				run()
			}
		}
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
	
	private fun loadScripts(manual: Boolean = true) {
		if (manual) {
			// these should be loaded from the files and/or classpath
			scripts.add(AutoSmite())
			scripts.add(CooldownTracker())
			scripts.add(LastPositionTracker())
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
		@Suppress("ControlFlowWithEmptyBody")
		while (mouseLocation() == beforeMove);
		beforeReset()
		mouse(beforeMove.x, beforeMove.y)
	}
	
	inline fun mouse(vector2D: Vector2D, beforeReset: () -> Unit) =
		mouse(vector2D.x.toInt(), vector2D.y.toInt(), beforeReset)
	
	fun SpriteBatch.drawSprite(texture: Texture, x: Float, y: Float, width: Float, height: Float) =
		draw(texture, x, y, width, height, 0, 0, texture.width, texture.height, false, true)
	
	fun SpriteBatch.setDarkness(percent: Float) = setColor(percent, percent, percent, 1F)
	
	fun BitmapFont.text(text: String, x: Float, y: Float, batch: SpriteBatch = overlay.sprites) =
		draw(batch, text, x, y)
	
	inline fun Vector2D.use(ifOnScreen: Vector2D.() -> Unit) {
		if (renderer.onScreen(this))
			ifOnScreen()
	}
	
	val time get() = gameTime.gameTime
	
	val me get() = localPlayer.localPlayer
	
}