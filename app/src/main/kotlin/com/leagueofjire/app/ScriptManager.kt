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