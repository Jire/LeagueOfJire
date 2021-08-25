package com.leagueofjire.core.game

import org.jire.kna.Pointer
import org.jire.kna.ReadableSource

class RiotStrings private constructor() {
	
	companion object {
		const val DEFAULT_STRING = ""
		const val DEFAULT_STRING_LENGTH = 50
		
		private val threadLocal = ThreadLocal.withInitial { RiotStrings() }
		
		operator fun invoke(): RiotStrings = threadLocal.get()
	}
	
	private val byteArray = ByteArray(DEFAULT_STRING_LENGTH)
	private val pointer = Pointer.alloc(DEFAULT_STRING_LENGTH.toLong())
	private val stringBuilder = StringBuilder()
	
	fun byteArrayToRiotString(byteArray: ByteArray): String {
		stringBuilder.setLength(0)
		for (b in byteArray) {
			val c = b.toInt() and 0xFF
			if (c == 0) break
			if (c > Byte.MAX_VALUE) return ""
			stringBuilder.append(c.toChar().lowercaseChar())
		}
		return stringBuilder.toString()
	}
	
	fun getRiotString(offset: Long = 0, bytes: Int = DEFAULT_STRING_LENGTH): String {
		if (!pointer.readable()) return DEFAULT_STRING
		
		pointer.read(offset, byteArray, 0, bytes)
		return byteArrayToRiotString(byteArray)
	}
	
	fun riotString(readableSource: ReadableSource, address: Long, bytes: Int = DEFAULT_STRING_LENGTH): String {
		if (!readableSource.read(address, pointer, bytes.toLong())) return DEFAULT_STRING
		return getRiotString(0, bytes)
	}
	
}