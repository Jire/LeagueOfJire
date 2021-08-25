package com.leagueofjire.core.util

import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs

class FastRandom(var seed: Long = ThreadLocalRandom.current().nextLong()) {
	
	operator fun get(max: Int): Int {
		seed = seed xor (seed shr 12)
		seed = seed xor (seed shl 25)
		seed = seed xor (seed shr 27)
		seed *= 2685821657736338717L
		
		val factor = abs(seed) / Long.MAX_VALUE.toDouble()
		return (max * factor).toInt()
	}
	
}