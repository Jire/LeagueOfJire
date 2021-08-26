package com.leagueofjire.core.game

import com.leagueofjire.ScreenPosition
import com.leagueofjire.core.game.unit.IGameUnit
import com.leagueofjire.core.offsets.LViewOffsets
import com.leagueofjire.game.GameMinimap
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object IGameMinimap : GameMinimap {
	
	private val data = Pointer.alloc(0x80)
	
	override var x = -1F
	override var y = -1F
	
	override var width = -1F
	override var height = -1F
	override fun update(): Boolean {
		TODO("Not yet implemented")
	}
	
	override fun get(x: Float, y: Float, z: Float) = screen(x, y, z)
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val minimapObject = process.int(base.address + LViewOffsets.MinimapObject).toLong()
		if (minimapObject <= 0) return false
		
		val minimapHUD = process.int(minimapObject + LViewOffsets.MinimapObjectHud).toLong()
		if (minimapHUD <= 0) return false
		
		if (!process.read(minimapHUD, data, 0x80)) return false
		if (!data.readable()) return false
		
		x = data.getFloat(LViewOffsets.MinimapHudPos)
		y = data.getFloat(LViewOffsets.MinimapHudPos + 4)
		
		width = data.getFloat(LViewOffsets.MinimapHudSize)
		height = data.getFloat(LViewOffsets.MinimapHudSize + 4)
		
		return true
	}
	
	const val WORLD_SCALE = 15000F
	
	fun screen(x: Float, y: Float, z: Float): ScreenPosition {
		var rx = x / WORLD_SCALE
		var ry = z / WORLD_SCALE
		rx = IGameMinimap.x + rx * width
		ry = IGameMinimap.y + height - (ry * height)
		return ScreenPosition(rx, ry)
	}
	
	fun screen(unit: IGameUnit) = screen(unit.position.x, unit.position.y, unit.position.z)
	
	fun distanceToMinimap(dist: Float) = (dist / WORLD_SCALE) * width
	
}