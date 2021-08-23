package com.leagueofjire.game

import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object Minimap {
	
	private val data = Pointer.alloc(0x80)
	
	var x = -1F
	var y = -1F
	
	var width = -1F
	var height = -1F
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val minimapObject = process.int(base.address + Offsets.MinimapObject).toLong()
		if (minimapObject <= 0) return false
		
		val minimapHUD = process.int(minimapObject + Offsets.MinimapObjectHud).toLong()
		if (minimapHUD <= 0) return false
		
		if (!process.read(minimapHUD, data, 0x80)) return false
		if (!data.readable()) return false
		
		x = data.getFloat(Offsets.MinimapHudPos)
		y = data.getFloat(Offsets.MinimapHudPos + 4)
		
		width = data.getFloat(Offsets.MinimapHudSize)
		height = data.getFloat(Offsets.MinimapHudSize + 4)
		
		return true
	}
	
	const val WORLD_SCALE = 15000F
	
	fun screen(x: Float, y: Float, z: Float): Vector2D {
		var rx = x / WORLD_SCALE
		var ry = z / WORLD_SCALE
		rx = this.x + rx * width
		ry = this.y + height - (ry * height)
		return Vector2D(rx, ry)
	}
	
	fun screen(unit: Unit) = screen(unit.x, unit.y, unit.z)
	
	fun distanceToMinimap(dist: Float) = (dist / WORLD_SCALE) * width
	
}