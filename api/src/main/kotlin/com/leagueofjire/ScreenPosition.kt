package com.leagueofjire

@JvmInline
value class ScreenPosition(val bitpack: Long) {
	constructor(x: Int, y: Int) : this(bitpack(x, y))
	constructor(x: Float, y: Float) : this(x.toInt(), y.toInt())
	
	val x get() = bitpackA(bitpack)
	val y get() = bitpackB(bitpack)
	
	operator fun component1() = x
	operator fun component2() = y
	
	companion object {
		
		internal fun bitpack(a: Int, b: Int): Long {
			val aMasked = a.toLong() and 0xFFFF_FFFF
			val bMasked = (b.toLong() and 0xFFFF_FFFF) shl 32
			return aMasked or bMasked
		}
		
		internal fun bitpackA(bitpack: Long) = (bitpack and 0xFFFF_FFFF).toInt()
		internal fun bitpackB(bitpack: Long) = ((bitpack ushr 32) and 0xFFFF_FFFF).toInt()
		
	}
	
}