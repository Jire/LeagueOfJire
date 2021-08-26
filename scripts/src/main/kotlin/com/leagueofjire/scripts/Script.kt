/*
 * LeagueOfJire: Free & Open-Source External Scripting Platform
 * Copyright (C) 2021  Thomas G. P. Nappo (Jire)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.leagueofjire.ScreenPosition
import com.leagueofjire.core.game.IGameContext
import com.leagueofjire.game.GameContext
import com.leagueofjire.game.unit.GameUnit
import com.leagueofjire.game.unit.champion.GameChampion
import com.leagueofjire.input.KeyboardContext
import com.leagueofjire.input.MouseContext
import com.leagueofjire.overlay.OverlayContext
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
	"LeagueOfJire Script",
	fileExtension = "leagueofjire.kts",
	compilationConfiguration = MyScriptCompilationConfiguration::class,
	//evaluationConfiguration = MyScriptEvaluationConfiguration::class
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
		for (i in 0..doRenders.lastIndex)
			doRenders[i](overlayContext)
		
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
	
	private val eachUnits: ObjectList<GameUnit.() -> Unit> = ObjectArrayList()
	
	fun eachUnit(eachUnit: GameUnit.() -> Unit) {
		eachUnits.add(eachUnit)
	}
	
	private val eachChampions: ObjectList<GameChampion.() -> Unit> = ObjectArrayList()
	
	fun eachChampion(eachChampion: GameChampion.() -> Unit) {
		eachChampions.add(eachChampion)
	}
	
	inline fun ScreenPosition.use(crossinline ifOnScreen: ScreenPosition.() -> Unit) {
		if (gameContext.renderer.onScreen(this))
			ifOnScreen()
	}
	
	fun Texture.draw(x: Float, y: Float, width: Float, height: Float) = sprites.drawSprite(this, x, y, width, height)
	
	fun SpriteBatch.drawSprite(
		texture: Texture,
		x: Float, y: Float,
		width: Float = texture.width.toFloat(), height: Float = texture.height.toFloat()
	) = draw(texture, x, y, width, height, 0, 0, texture.width, texture.height, false, true)
	
	fun SpriteBatch.setDarkness(percent: Float) = setColor(percent, percent, percent, 1F)
	
	fun BitmapFont.text(text: String, x: Float, y: Float, batch: SpriteBatch = sprites) =
		draw(batch, text, x, y)
	
	fun BitmapFont.text(text: String, x: Int, y: Int, batch: SpriteBatch = sprites) =
		draw(batch, text, x.toFloat(), y.toFloat())
	
	fun BitmapFont.text(text: String, screenPosition: ScreenPosition, batch: SpriteBatch = sprites) =
		text(text, screenPosition.x, screenPosition.y, batch)
	
}