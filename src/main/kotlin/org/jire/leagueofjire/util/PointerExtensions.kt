package org.jire.leagueofjire.util

import org.jire.kna.Pointer

fun Pointer.getRiotString(offset: Long = 0, bytes: Int = RiotStrings.DEFAULT_STRING_LENGTH): String {
	if (!readable()) return RiotStrings.DEFAULT_STRING
	
	val data = ByteArray(bytes)
	read(offset, data, 0, data.size)
	
	return RiotStrings.byteArrayToRiotString(data)
}