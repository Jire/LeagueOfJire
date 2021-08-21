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
	
	fun update(process: AttachedProcess, base: AttachedModule): Boolean {
		val renderBase = process.int(base.address + Offsets.Renderer).toLong()
		if (renderBase <= 0) return false
		
		val data = process.readPointer(renderBase, 128)
		if (!data.readable()) return false
		
		width = data.getInt(LViewOffsets.RendererWidth)
		height = data.getInt(LViewOffsets.RendererHeight)
		
		val viewData = process.readPointer(base.address + Offsets.ViewMatrix, 128)
		return updateMatrix(viewData)
	}
	
	private fun updateMatrix(viewData: Pointer): Boolean {
		if (!viewData.readable()) return false
		
		for (i in 0..viewMatrix.lastIndex)
			viewMatrix[i] = viewData.getFloat(i.toLong() * Float.SIZE_BYTES)
		for (i in 0..projMatrix.lastIndex)
			projMatrix[i] = viewData.getFloat(64L + (i * Float.SIZE_BYTES))
		
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
	
	fun worldToScreen(x: Float, y: Float, z: Float): Pair<Float, Float> {
		val clipCoordsX = x * viewProjMatrix[0] + y * viewProjMatrix[4] + z * viewProjMatrix[8] + viewProjMatrix[12]
		val clipCoordsY = x * viewProjMatrix[1] + y * viewProjMatrix[5] + z * viewProjMatrix[9] + viewProjMatrix[13]
		//var clipCoordsZ = x * viewProjMatrix[2] + y * viewProjMatrix[6] + z * viewProjMatrix[10] + viewProjMatrix[14]
		var clipCoordsW = x * viewProjMatrix[3] + y * viewProjMatrix[7] + z * viewProjMatrix[11] + viewProjMatrix[15]
		
		if (clipCoordsW < 1) clipCoordsW = 1F
		
		val middleX = clipCoordsX / clipCoordsW
		val middleY = clipCoordsY / clipCoordsW
		//val middleZ = clipCoordsZ / clipCoordsW
		
		val screenX = width.toFloat()
		val screenY = height.toFloat()
		
		val outX = (screenX / 2F * middleX) + (middleX + screenX / 2F)
		val outY = -(screenY / 2F * middleY) + (middleY + screenY / 2F)
		return outX to outY
	}
	
	fun isScreenPointOnScreen(x: Float, y: Float, offsetX: Float, offsetY: Float) =
		x > -offsetX && x < (width + offsetX) && y > -offsetY && y < (height + offsetY)
	
	fun isWorldPointOnScreen(x: Float, y: Float, z: Float, offsetX: Float, offsetY: Float): Boolean {
		val w2s = worldToScreen(x, y, z)
		return isScreenPointOnScreen(w2s.first, w2s.second, offsetX, offsetY)
	}
	
	fun distanceToMinimap(dist: Float, wSizeX: Float, wSizeY: Float) = (dist / 15000F) * wSizeX
	
	fun worldToMinimap(
		x: Float,
		y: Float,
		z: Float,
		wPosX: Float,
		wPosY: Float,
		wSizeX: Float,
		wSizeY: Float
	): Pair<Float, Float> {
		var rx = x / 15000F
		var ry = z / 15000F
		rx = wPosX + rx * wSizeX
		ry = wPosY + wSizeY - (ry * wSizeY)
		return rx to ry
	}
	
}