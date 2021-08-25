package com.leagueofjire

interface Vector2 : Vector {
	
	val a: Float
	val b: Float
	
	operator fun component1() = a
	operator fun component2() = b
	
}