val toggleKey: Int = KeyEvent.VK_BACK_SPACE

var lastToggle = -1F
var enabled = false

render {
	val time = gameTime.time
	if (time - lastToggle >= 0.3F && Keyboard.keyPressed(toggleKey)) {
		lastToggle = time
		enabled = !enabled
	}
	
	val color = if (enabled) Color.GREEN else Color.RED
	shapes.color = color
	val y = renderer.height / 2F
	shapes.rect(2F, y - 2F, 72F, 18F)
	font.color = color
	font.text("AutoSmite", 5F, y)
}

eachUnit {
	if (!enabled || !isVisible || !isAlive || !info.isImportantJungle) return@eachUnit
	
	if (!me.isAlive) return@eachUnit
	
	val gameTime = gameTime.time
	
	val smite = me.spells[5]
	if (health > smite.value || !smite.canCast(gameTime)/* || !withinDistance(player, smite.info.castRange)*/) return@eachUnit
	
	mouse(renderer.screen(this)) {
		key(KeyEvent.VK_F)
	}
}