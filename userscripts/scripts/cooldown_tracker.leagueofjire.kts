val iconSize = 28F
val readyDarkness = 0.8F
val unreadyDarkness = 0.4F
val textColor: Color = Color.WHITE

val yOffset = iconSize * 2

val xTextOffset = -iconSize + (iconSize / 4) - 4
val yTextOffset = (iconSize / 2) + 2

eachChampion {
	if (isVisible && isAlive) renderer[this].use {
		val drawY = y + yOffset
		var xOffset = -yOffset
		
		for (spell in spells) {
			spell.draw(x + xOffset, drawY)
			xOffset += iconSize
		}
	}
}

fun GameChampionSpell.draw(x: Float, y: Float) {
	val sprite = sprite ?: return
	
	val levelled = level >= 1
	val remaining = readyAtSeconds - time.seconds
	val ready = remaining <= 0
	
	if (!levelled || !ready) sprites.setDarkness(unreadyDarkness)
	
	sprite.draw(x - iconSize, y, iconSize, iconSize)
	sprites.setDarkness(readyDarkness)
	
	if (levelled && !ready) {
		font.color = textColor
		font.text(remainingToString(remaining), x + xTextOffset, y + yTextOffset)
	}
}

fun remainingToString(remaining: Float) =
	if (remaining < 1) remaining.round(1).toString()
	else remaining.toInt().toString()