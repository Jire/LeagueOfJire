package com.leagueofjire.game

import org.jire.kna.Pointer
import org.jire.kna.ReadableSource

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
	
	fun Pointer.getRiotString(offset: Long = 0, bytes: Int = RiotStrings.DEFAULT_STRING_LENGTH): String {
		if (!readable()) return RiotStrings.DEFAULT_STRING
		
		val data = ByteArray(bytes)
		read(offset, data, 0, data.size)
		
		return RiotStrings.byteArrayToRiotString(data)
	}
	
	fun ReadableSource.riotString(address: Long, bytes: Int = RiotStrings.DEFAULT_STRING_LENGTH): String {
		val pointer = readPointer(address, bytes.toLong())
		return pointer.getRiotString(0, bytes)
	}
	
}