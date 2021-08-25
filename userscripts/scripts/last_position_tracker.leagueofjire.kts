import kotlin.math.min

val onMinimap = true
val onMinimapColor: Color = Color.WHITE
val onMinimapChars = 3

val onWorld = true
val onWorldColor: Color = Color.WHITE

eachChampion {
	if (!isVisible) {
		val timeMissing = (time.seconds - lastVisibleTime).toInt()
		
		if (onMinimap) minimap[this].use {
			val shortName = name.substring(0, min(onMinimapChars, name.length))
			font.color = onMinimapColor
			font.text("$shortName $timeMissing", this)
		}
		
		if (onWorld) renderer[this].use {
			font.color = onWorldColor
			font.text("$name $timeMissing", this)
		}
	}
}