package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.leagueofjire.game.*
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
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
	val gameContext: GameContext,
	val overlayContext: OverlayContext,
	val robot: Robot
) : GameContext by gameContext,
	OverlayContext by overlayContext {
	
	private val doRenders: ObjectList<OverlayContext.() -> Unit> = ObjectArrayList()
	
	fun render(doRender: OverlayContext.() -> Unit) {
		doRenders.add(doRender)
	}
	
	fun render() {
		for (i in 0..doRenders.lastIndex) doRenders[i](overlayContext)
		
		for (i in 0..unitManager.champions.lastIndex) {
			val champion = unitManager.champions[i] ?: continue
			for (ci in 0..eachChampions.lastIndex)
				eachChampions[ci](champion)
		}
		for (entry in unitManager.unitsIt) {
			val unit = entry.value
			for (i in 0..eachUnits.lastIndex)
				eachUnits[i](unit)
		}
	}
	
	private val eachUnits: ObjectList<UnitHook> = ObjectArrayList()
	
	fun eachUnit(eachUnit: UnitHook) {
		eachUnits.add(eachUnit)
	}
	
	private val eachChampions: ObjectList<UnitHook> = ObjectArrayList()
	
	fun eachChampion(eachChampion: UnitHook) {
		eachChampions.add(eachChampion)
	}
	
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
	
	fun BitmapFont.text(text: String, x: Float, y: Float, batch: SpriteBatch = overlayContext.sprites) =
		draw(batch, text, x, y)
	
}