import com.leagueofjire.util.round

val iconSize: Float = 28F
val readyDarkness: Float = 0.8F
val unreadyDarkness: Float = 0.4F
val textColor: Color = Color.WHITE

val yOffset = iconSize * 2

val xTextOffset = -iconSize + (iconSize / 4) - 4
val yTextOffset = (iconSize / 2) + 2

eachChampion {
	if (isVisible && isAlive) renderer.screen(this).use {
		val drawY = y + yOffset
		var xOffset = -yOffset
		
		for (spell in spells) {
			drawSpell(spell, x + xOffset, drawY)
			xOffset += iconSize
		}
	}
}

fun drawSpell(spell: Spell, x: Float, y: Float) {
	val spellData = spell.info
	if (spellData === SpellInfo.unknownSpell) return
	val icon = spellData.loadIcon ?: return
	
	val levelled = spell.level >= 1
	val remaining = spell.readyAt - gameTime.time
	val ready = remaining <= 0
	
	if (!levelled || !ready) sprites.setDarkness(unreadyDarkness)
	
	sprites.drawSprite(icon, x - iconSize, y, iconSize, iconSize)
	sprites.setDarkness(readyDarkness)
	
	if (levelled && !ready) {
		font.color = textColor
		font.text(remainingToString(remaining), x + xTextOffset, y + yTextOffset)
	}
}

fun remainingToString(remaining: Float) =
	if (remaining < 1) remaining.round(1).toString()
	else remaining.toInt().toString()