package org.jire.leagueofjire

import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.model.Champion
import org.jire.leagueofjire.model.Renderer
import org.jire.leagueofjire.offsets.Offsets

object FindLocalPlayer {
	
	fun find(lol: AttachedProcess, base: Long, renderer: Renderer): Champion? {
		val playerAddress = lol.int(base + Offsets.LocalPlayer).toLong()
		if (playerAddress <= 0) return null
		val player = Champion(playerAddress)
		val playerBuff = player.load(lol, true, renderer, player) ?: return null
		if (!player.loadChampion(playerBuff, lol, true)) return null
		return player
	}
	
}