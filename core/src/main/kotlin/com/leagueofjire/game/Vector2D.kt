package com.leagueofjire.game

@JvmInline
value class Vector2D(val combo: Long) {
	
	constructor(x: Float, y: Float) : this(combo(x, y))
	
	val x get() = comboA(combo)
	val y get() = comboB(combo)
	
	operator fun component1() = x
	operator fun component2() = y
	
	companion object {
		
		fun combo(a: Float, b: Float): Long {
			val aMasked = a.toLong() and 0xFFFF_FFFF
			val bMasked = (b.toLong() and 0xFFFF_FFFF) shl 32
			return aMasked or bMasked
		}
		
		fun comboA(combo: Long) = (combo and 0xFFFF_FFFF).toFloat()
		fun comboB(combo: Long) = ((combo ushr 32) and 0xFFFF_FFFF).toFloat()
		
	}
	
}