package com.leagueofjire.game

import com.leagueofjire.game.offsets.LViewOffsets
import com.leagueofjire.game.offsets.Offsets
import org.jire.kna.Pointer
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int

object Renderer {
	
	var width = -1
	var height = -1
	
	val viewMatrix = FloatArray(16)
	val projMatrix = FloatArray(16)
	val viewProjMatrix = FloatArray(16)
	
	private val data = Pointer.alloc(128)
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val renderBase = process.int(base.address + Offsets.Renderer).toLong()
		if (renderBase <= 0) return false
		
		if (!process.read(renderBase, data, 128)) return false
		if (!data.readable()) return false
		
		width = data.getInt(LViewOffsets.RendererWidth)
		height = data.getInt(LViewOffsets.RendererHeight)
		
		if (!process.read(base.address + Offsets.ViewMatrix, data, 128)) return false
		return updateMatrix(data)
	}
	
	private fun updateMatrix(@Suppress("SameParameterValue") data: Pointer): Boolean {
		if (!data.readable()) return false
		
		for (i in 0..viewMatrix.lastIndex)
			viewMatrix[i] = data.getFloat(i.toLong() * Float.SIZE_BYTES)
		for (i in 0..projMatrix.lastIndex)
			projMatrix[i] = data.getFloat(64L + (i * Float.SIZE_BYTES))
		
		return sumMatrix()
	}
	
	private fun sumMatrix(): Boolean {
		for (i in 0..3) {
			for (j in 0..3) {
				var sum = 0F
				for (k in 0..3) sum += viewMatrix[i * 4 + k] * projMatrix[k * 4 + j]
				viewProjMatrix[i * 4 + j] = sum
			}
		}
		return true
	}
	
	fun screen(unit: Unit) = screen(unit.x, unit.y, unit.z)
	
	fun screen(x: Float, y: Float, z: Float): Vector2D {
		val coordX = x * viewProjMatrix[0] + y * viewProjMatrix[4] + z * viewProjMatrix[8] + viewProjMatrix[12]
		val coordY = x * viewProjMatrix[1] + y * viewProjMatrix[5] + z * viewProjMatrix[9] + viewProjMatrix[13]
		var coordW = x * viewProjMatrix[3] + y * viewProjMatrix[7] + z * viewProjMatrix[11] + viewProjMatrix[15]
		
		if (coordW < 1) coordW = 1F
		
		val middleX = coordX / coordW
		val middleY = coordY / coordW
		
		val screenX = (width / 2F * middleX) + (middleX + width / 2F)
		val screenY = -(height / 2F * middleY) + (middleY + height / 2F)
		
		return Vector2D(screenX, screenY)
	}
	
	fun onScreen(vector2D: Vector2D) = onScreen(vector2D.x, vector2D.y)
	
	fun onScreen(x: Float, y: Float, offsetX: Float = 0F, offsetY: Float = 0F) =
		x > -offsetX && x < (width + offsetX) && y > -offsetY && y < (height + offsetY)
	
}