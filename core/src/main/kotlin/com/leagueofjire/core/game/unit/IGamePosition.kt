package com.leagueofjire.core.game.unit

import com.leagueofjire.game.GamePosition

data class IGamePosition(
	override var x: Float = -1F,
	override var y: Float = -1F,
	override var z: Float = -1F
) : GamePosition