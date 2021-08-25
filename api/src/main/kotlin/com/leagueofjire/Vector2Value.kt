package com.leagueofjire

@JvmInline
value class Vector2Value(val bitpack: Long) : Vector2 {
	constructor(a: Float, b: Float) : this(bitpack(a, b))
	
	override val a get() = bitpackA(bitpack)
	override val b get() = bitpackB(bitpack)
	
	companion object {
		
		fun bitpack(a: Float, b: Float): Long {
			val aMasked = a.toLong() and 0xFFFF_FFFF
			val bMasked = (b.toLong() and 0xFFFF_FFFF) shl 32
			return aMasked or bMasked
		}
		
		fun bitpackA(bitpack: Long) = (bitpack and 0xFFFF_FFFF).toFloat()
		fun bitpackB(bitpack: Long) = ((bitpack ushr 32) and 0xFFFF_FFFF).toFloat()
		
	}
	
}