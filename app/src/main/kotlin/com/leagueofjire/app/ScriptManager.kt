package com.leagueofjire.app

import com.leagueofjire.game.*
import com.leagueofjire.scripts.DefaultOverlayContext
import com.leagueofjire.scripts.OverlayContext
import com.leagueofjire.scripts.Script
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.Robot

class ScriptManager(val gameContext: GameContext, val overlay: Overlay) {
	
	private val scripts: ObjectList<Script> = ObjectArrayList()
	
	fun render() {
		overlay.sprites.begin()
		overlay.shapes.begin()
		
		for (si in 0..scripts.lastIndex) scripts[si].render()
		
		overlay.sprites.end()
		overlay.shapes.end()
	}
	
	fun load(
		overlayContext: OverlayContext = DefaultOverlayContext(overlay.sprites, overlay.shapes, overlay.texts),
		robot: Robot = Robot().apply { autoDelay = 1; isAutoWaitForIdle = true }
	) {
		ClassGraph().enableClassInfo().scan().use { scanResult ->
			val scriptClassList = scanResult.getSubclasses(Script::class.java).directOnly()
			scriptClassList.forEach {
				val scriptClass = it.loadClass(Script::class.java)
				val constructor = scriptClass.getDeclaredConstructor(
					GameContext::class.java,
					OverlayContext::class.java,
					Robot::class.java
				)
				val script = constructor.newInstance(gameContext, overlayContext, robot)
				scripts.add(script)
			}
		}
	}
	
}