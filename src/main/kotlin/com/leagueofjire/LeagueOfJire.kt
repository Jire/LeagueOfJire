package com.leagueofjire

import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import com.leagueofjire.overlay.OverlayManager
import com.leagueofjire.scripts.ScriptManager
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		configureMainThread()
		cycleLoop()
	}
	
	private fun configureMainThread() = Thread.currentThread().apply {
		name = "LeagueOfJire"
		priority = Thread.MAX_PRIORITY
	}
	
	private const val CYCLE_MILLIS = 16L
	
	private fun cycleLoop(cycleMillis: Long = CYCLE_MILLIS) {
		UnitData.load()
		SpellData.load()
		
		val (process, base) = LeagueOfLegendsHook.hook()
		
		OverlayManager.open()
		
		while (!Thread.interrupted()) {
			val elapsed = measureTimeMillis {
				try {
					if (cycle(process, base)) {
					}
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
			val sleepMillis = cycleMillis - elapsed
			if (sleepMillis > 0) Thread.sleep(sleepMillis)
		}
	}
	
	private fun cycle(process: AttachedProcess, base: AttachedModule): Boolean {
		return GameTime.update(process, base)
				&& Renderer.update(process, base)
				&& UnitManager.update(process, base)
				&& LocalPlayer.update(process, base)
				&& HoveredObject.update(process, base)
				&& ScriptManager.run()
	}
	
}