import kotlin.math.min

val onMinimap: Boolean = true
val onMinimapColor: Color = Color.WHITE
val onMinimapChars: Int = 3

val onWorld: Boolean = true
val onWorldColor: Color = Color.WHITE

eachChampion {
	if (isVisible || !info.isChampion || name.isEmpty()) return@eachChampion
	
	val timeMissing = (gameTime.gameTime - lastVisibleAt).toInt()
	
	if (onMinimap) minimap.screen(this).use {
		val shortName = name.substring(0, min(onMinimapChars, name.length))
		overlay.texts.color = onMinimapColor
		overlay.texts.text("$shortName $timeMissing", x, y)
	}
	
	if (onWorld) renderer.screen(this).use {
		overlay.texts.color = onWorldColor
		overlay.texts.text("$name $timeMissing", x, y)
	}
}