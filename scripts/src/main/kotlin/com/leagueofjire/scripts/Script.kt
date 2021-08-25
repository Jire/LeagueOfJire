package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.leagueofjire.ScreenPosition
import com.leagueofjire.core.game.IGameContext
import com.leagueofjire.game.GameContext
import com.leagueofjire.input.KeyboardContext
import com.leagueofjire.input.MouseContext
import com.leagueofjire.overlay.OverlayContext
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
	val gameContext: IGameContext,
	private val overlayContext: OverlayContext,
	val mouse: MouseContext,
	val keyboard: KeyboardContext,
) : GameContext by gameContext,
	OverlayContext by overlayContext {
	
	private val doRenders: ObjectList<OverlayContext.() -> Unit> = ObjectArrayList()
	
	fun render(doRender: OverlayContext.() -> Unit) {
		doRenders.add(doRender)
	}
	
	fun render() {
		for (i in 0..doRenders.lastIndex) doRenders[i](overlayContext)
		
		for (i in 0..gameContext.unitManager.champions.lastIndex) {
			val champion = gameContext.unitManager.champions[i] ?: continue
			for (ci in 0..eachChampions.lastIndex)
				eachChampions[ci](champion)
		}
		for (entry in gameContext.unitManager.unitsIt) {
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
	
	inline fun ScreenPosition.use(ifOnScreen: ScreenPosition.() -> Unit) {
		if (gameContext.renderer.onScreen(this))
			ifOnScreen()
	}
	
	fun SpriteBatch.drawSprite(texture: Texture, x: Float, y: Float, width: Float, height: Float) =
		draw(texture, x, y, width, height, 0, 0, texture.width, texture.height, false, true)
	
	fun SpriteBatch.setDarkness(percent: Float) = setColor(percent, percent, percent, 1F)
	
	fun BitmapFont.text(text: String, x: Float, y: Float, batch: SpriteBatch = overlayContext.sprites) =
		draw(batch, text, x, y)
	
	fun BitmapFont.text(text: String, x: Int, y: Int, batch: SpriteBatch = overlayContext.sprites) =
		draw(batch, text, x.toFloat(), y.toFloat())
	
	fun BitmapFont.text(text: String, screenPosition: ScreenPosition, batch: SpriteBatch = overlayContext.sprites) =
		text(text, screenPosition.x, screenPosition.y, batch)
	
}