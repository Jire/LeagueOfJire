/*
 * LeagueOfJire: Free & Open-Source External Scripting Platform
 * Copyright (C) 2021  Thomas G. P. Nappo (Jire)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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