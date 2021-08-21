package org.jire.leagueofjire

import org.jire.kna.attach.AttachedProcess
import org.jire.kna.int
import org.jire.leagueofjire.model.Renderer
import org.jire.leagueofjire.offsets.LViewOffsets
import org.jire.leagueofjire.offsets.Offsets

object RendererReader {
	
	fun read(lol: AttachedProcess, moduleBase: Long): Renderer? {
		val renderBase = lol.int(moduleBase + Offsets.Renderer).toLong()
		if (renderBase <= 0) return null
		
		val renderer = lol.readPointer(renderBase, 128)
		if (!renderer.readable()) return null
		
		val width = renderer.getInt(LViewOffsets.RendererWidth)
		val height = renderer.getInt(LViewOffsets.RendererHeight)
		//println("$width, $height")
		
		val view = lol.readPointer(moduleBase + Offsets.ViewMatrix, 128)
		if (!view.readable()) return null
		
		val viewMatrix = FloatArray(16)
		for (i in 0..viewMatrix.lastIndex)
			viewMatrix[i] = view.getFloat(i.toLong() * Float.SIZE_BYTES)
		val projMatrix = FloatArray(16)
		for (i in 0..projMatrix.lastIndex)
			projMatrix[i] = view.getFloat(64L + (i * Float.SIZE_BYTES))
		
		val viewProjMatrix = FloatArray(16)
		val row1 = 4
		val col1 = 4
		val col2 = 4
		for (i in 0..row1 - 1) {
			for (j in 0..col2 - 1) {
				var sum = 0F
				for (k in 0..col1 - 1) sum += viewMatrix[i * col1 + k] * projMatrix[k * col2 + j]
				viewProjMatrix[i * col2 + j] = sum
			}
		}
		
		return Renderer(width, height, viewMatrix, projMatrix, viewProjMatrix)
	}
	
}