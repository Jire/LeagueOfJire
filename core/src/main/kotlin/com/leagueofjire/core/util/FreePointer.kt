package com.leagueofjire.core.util

import net.openhft.chronicle.core.OS
import org.jire.kna.Pointer

fun Pointer.free(size: Long) {
	OS.memory().freeMemory(address, size)
}