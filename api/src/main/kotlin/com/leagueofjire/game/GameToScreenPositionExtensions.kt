package com.leagueofjire.game

import com.leagueofjire.game.unit.GameUnit

operator fun GameToScreenPosition.get(position: GamePosition) = get(position.x, position.y, position.z)

operator fun GameToScreenPosition.get(unit: GameUnit) = get(unit.position)