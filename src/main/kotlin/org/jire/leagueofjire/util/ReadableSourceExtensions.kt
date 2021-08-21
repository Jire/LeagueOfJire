package org.jire.leagueofjire.util

import org.jire.kna.ReadableSource

fun ReadableSource.riotString(address: Long, bytes: Int = RiotStrings.DEFAULT_STRING_LENGTH): String {
	val pointer = readPointer(address, bytes.toLong())
	return pointer.getRiotString(0, bytes)
}