package com.leagueofjire.core.util

import kotlin.math.round

fun Float.round(decimals: Int): Float {
	var multiplier = 1F
	repeat(decimals) { multiplier *= 10 }
	return round(this * multiplier) / multiplier
}