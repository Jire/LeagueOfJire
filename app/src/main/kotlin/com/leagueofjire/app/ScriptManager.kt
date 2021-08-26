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

package com.leagueofjire.app

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.leagueofjire.core.game.IGameContext
import com.leagueofjire.core.input.IKeyboardContext
import com.leagueofjire.core.input.IMouseContext
import com.leagueofjire.input.KeyboardContext
import com.leagueofjire.input.MouseContext
import com.leagueofjire.overlay.OverlayContext
import com.leagueofjire.scripts.Script
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.Robot

class ScriptManager(val gameContext: IGameContext, val overlay: Overlay) {
	
	private val scripts: ObjectList<Script> = ObjectArrayList()
	
	fun render() {
		overlay.sprites.begin()
		overlay.shapes.begin()
		
		for (si in 0..scripts.lastIndex) scripts[si].render()
		
		overlay.sprites.end()
		overlay.shapes.end()
	}
	
	class DefaultOverlayContext(
		override val sprites: SpriteBatch,
		override val shapes: ShapeRenderer,
		override val font: BitmapFont
	) : OverlayContext
	
	fun load(
		overlayContext: OverlayContext = DefaultOverlayContext(overlay.sprites, overlay.shapes, overlay.texts),
		robot: Robot = Robot().apply { autoDelay = 1; isAutoWaitForIdle = true }
	) {
		val mouse = IMouseContext(robot)
		val keyboard = IKeyboardContext(robot)
		ClassGraph().enableClassInfo().scan().use { scanResult ->
			val scriptClassList = scanResult.getSubclasses(Script::class.java).directOnly()
			scriptClassList.forEach {
				val scriptClass = it.loadClass(Script::class.java)
				val constructor = scriptClass.getDeclaredConstructor(
					IGameContext::class.java,
					OverlayContext::class.java,
					MouseContext::class.java,
					KeyboardContext::class.java
				)
				val script = constructor.newInstance(gameContext, overlayContext, mouse, keyboard)
				scripts.add(script)
			}
		}
	}
	
}