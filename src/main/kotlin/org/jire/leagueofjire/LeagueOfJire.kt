package org.jire.leagueofjire

import org.jire.kna.float
import org.jire.leagueofjire.native.LeagueOfLegendsHook
import org.jire.leagueofjire.offsets.Offsets
import kotlin.system.measureTimeMillis

object LeagueOfJire {
	
	@JvmStatic
	fun main(args: Array<String>) {
		println(Offsets.UnderMouseObject - 0x02FB51E8)
		
		val hook = LeagueOfLegendsHook.hook()
		val baseAddress = hook.baseAddress
		println("BASE MODULE OFFSET: $baseAddress")
		
		val lol = hook.process
		
		val gameTime = lol.float(baseAddress + Offsets.GameTime)
		println("game time: $gameTime")
		
		while (!Thread.interrupted()) {
			val elapsed = measureTimeMillis {
				val renderer = RendererReader.read(lol, baseAddress) ?: return@measureTimeMillis
				val player = FindLocalPlayer.find(lol, baseAddress, renderer) ?: return@measureTimeMillis
				//println(renderer.worldToScreen(player.x, player.y, player.z))
				ObjectReader.read(lol, baseAddress, renderer, player)
				//FindHoveredObject.find(lol, baseAddress)
			}
			val sleep = 16 - elapsed
			if (sleep > 0) {
				Thread.sleep(sleep)
			}
		}
	}
	
}