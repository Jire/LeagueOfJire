package com.leagueofjire.input

import com.leagueofjire.ScreenPosition

inline fun MouseContext.move(x: Int, y: Int, beforeReset: () -> Unit) {
	val beforeMove = position
	move(x, y)
	@Suppress("ControlFlowWithEmptyBody")
	while (position == beforeMove);
	beforeReset()
	move(beforeMove.x, beforeMove.y)
}

inline fun MouseContext.move(screenPosition: ScreenPosition, beforeReset: () -> Unit) =
	move(screenPosition.x, screenPosition.y, beforeReset)