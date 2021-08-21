package com.leagueofjire.game

enum class SpellSlot(val slot: Int) {
	
	Q(0),
	W(1),
	E(2),
	R(3),
	D(4),
	F(5);
	
	companion object {
		val values = values()
		
		operator fun get(slot: Int) = values[slot]
	}
	
}