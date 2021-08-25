package com.leagueofjire.core.game

import com.leagueofjire.ScreenPosition

@JvmInline
value class Vector2D(val combo: Long) : ScreenPosition {
	
	constructor(x: Int, y: Int) : this(combo(x, y))
	constructor(x: Float, y: Float) : this(x.toInt(), y.toInt())
	
	override val x get() = comboA(combo)
	override val y get() = comboB(combo)
	
	operator fun component1() = x
	operator fun component2() = y
	
	companion object {
		
		fun combo(a: Int, b: Int): Long {
			val aMasked = a.toLong() and 0xFFFF_FFFF
			val bMasked = (b.toLong() and 0xFFFF_FFFF) shl 32
			return aMasked or bMasked
		}
		
		fun comboA(combo: Long) = (combo and 0xFFFF_FFFF).toInt()
		fun comboB(combo: Long) = ((combo ushr 32) and 0xFFFF_FFFF).toInt()
		
	}
	
}