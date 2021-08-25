package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.leagueofjire.game.*
import com.leagueofjire.overlay.Overlay
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
	"LeagueOfJire Script",
	fileExtension = "leagueofjire.kts",
	compilationConfiguration = MyScriptCompilationConfiguration::class,
	//evaluationConfiguration = JireScriptEvaluationConfiguration::class
)
abstract class Script(
	val scriptContext: ScriptContext,
	val overlay: Overlay,
	val gameTime: GameTime,
	val renderer: Renderer,
	val minimap: Minimap,
	val unitManager: UnitManager,
	val localPlayer: LocalPlayer,
	val hoveredUnit: HoveredUnit,
	val robot: Robot = Robot().apply { autoDelay = 1; isAutoWaitForIdle = true }
) {
	
	fun key(keycode: Int) {
		robot.keyPress(keycode)
		robot.keyRelease(keycode)
	}
	
	fun mouse(x: Int, y: Int) = robot.mouseMove(x, y)
	
	fun mouseLocation(): Point = MouseInfo.getPointerInfo().location
	
	inline fun mouse(x: Int, y: Int, beforeReset: () -> Unit) {
		val beforeMove = mouseLocation()
		mouse(x, y)
		@Suppress("ControlFlowWithEmptyBody")
		while (mouseLocation() == beforeMove);
		beforeReset()
		mouse(beforeMove.x, beforeMove.y)
	}
	
	inline fun mouse(vector2D: Vector2D, beforeReset: () -> Unit) =
		mouse(vector2D.x.toInt(), vector2D.y.toInt(), beforeReset)
	
	inline fun Vector2D.use(ifOnScreen: Vector2D.() -> Unit) {
		if (renderer.onScreen(this))
			ifOnScreen()
	}
	
	inline val me get() = localPlayer.localPlayer
	
	fun SpriteBatch.drawSprite(texture: Texture, x: Float, y: Float, width: Float, height: Float) =
		draw(texture, x, y, width, height, 0, 0, texture.width, texture.height, false, true)
	
	fun SpriteBatch.setDarkness(percent: Float) = setColor(percent, percent, percent, 1F)
	
	fun BitmapFont.text(text: String, x: Float, y: Float, batch: SpriteBatch = overlay.sprites) =
		draw(batch, text, x, y)
	
}