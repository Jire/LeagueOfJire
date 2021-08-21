package org.jire.leagueofjire.model

class Renderer(
	val width: Int, val height: Int,
	val viewMatrix: FloatArray, val projMatrix: FloatArray,
	val viewProjMatrix: FloatArray
) {
	
	fun worldToScreen(x: Float, y: Float, z: Float): Pair<Int, Int> {
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
		return outX.toInt() to outY.toInt()
	}
	
}