import com.leagueofjire.game.unit.champion.spell.GameChampionSpells

val toggleKey: Int = KeyEvent.VK_BACK_SPACE

var lastToggle = -1F
var enabled = false

render {
	val time = time.seconds
	if (time - lastToggle >= 0.3F && keyboard.pressed(toggleKey)) {
		lastToggle = time
		enabled = !enabled
	}
	
	val color = if (enabled) Color.GREEN else Color.RED
	shapes.color = color
	val y = renderer.height / 2
	shapes.rect(2F, y - 2F, 72F, 18F)
	font.color = color
	font.text("AutoSmite", 5, y)
}

eachUnit {
	val smite = me.spell(GameChampionSpells.SMITE) ?: return@eachUnit
	if (!enabled || !isVisible || !isAlive || !info.isImportantJungle) return@eachUnit
	
	if (!me.isAlive) return@eachUnit
	
	if (health > smite.value || !smite.canCast(time)/* || !withinDistance(player, smite.info.castRange)*/) return@eachUnit
	
	mouse.move(renderer[this]) {
		keyboard.pressAndRelease(KeyEvent.VK_F)
	}
}