package org.jire.leagueofjire.util

object RiotStrings {
	
	const val DEFAULT_STRING = ""
	const val DEFAULT_STRING_LENGTH = 50
	
	fun byteArrayToRiotString(byteArray: ByteArray): String {
		val sb = StringBuilder()
		for (b in byteArray) {
			val c = b.toInt() and 0xFF
			if (c == 0) break
			if (c > Byte.MAX_VALUE) return ""
			sb.append(c.toChar())
		}
		return sb.toString()
	}
	
}