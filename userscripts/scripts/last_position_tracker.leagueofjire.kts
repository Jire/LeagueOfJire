import kotlin.math.min

val onMinimap: Boolean = true
val onMinimapColor: Color = Color.WHITE
val onMinimapChars: Int = 3

val onWorld: Boolean = true
val onWorldColor: Color = Color.WHITE

eachChampion {
	if (isVisible || !info.isChampion || name.isEmpty()) return@eachChampion
	
	val timeMissing = (gameTime.time - lastVisibleAt).toInt()
	
	if (onMinimap) minimap.screen(this).use {
		val shortName = name.substring(0, min(onMinimapChars, name.length))
		font.color = onMinimapColor
		font.text("$shortName $timeMissing", x, y)
	}
	
	if (onWorld) renderer.screen(this).use {
		font.color = onWorldColor
		font.text("$name $timeMissing", x, y)
	}
}