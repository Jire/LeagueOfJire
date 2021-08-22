package com.leagueofjire.overlay

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.leagueofjire.game.*
import com.leagueofjire.game.Unit
import com.leagueofjire.native.User32
import com.leagueofjire.overlay.OverlayManager.myHWND
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt

object Overlay : ApplicationAdapter() {
	
	val title = ThreadLocalRandom.current().nextLong().toString()
	
	@Volatile
	var created: Boolean = false
	
	lateinit var batch: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapeRenderer: ShapeRenderer
	lateinit var textRenderer: BitmapFont
	
	lateinit var process: AttachedProcess
	lateinit var base: AttachedModule
	
	private fun updateGameState() {
		GameTime.update(process, base)
		Renderer.update(process, base)
		// minimap
		UnitManager.update(process, base)
		// clear missing objects
		//LocalPlayer.update(process, base)
		//HoveredObject.update(process, base)
		// get map, summoner's rift / howling etc.
	}
	
	private fun useUnit(unit: Unit) = unit.run {
		if (!isVisible || !isAlive || !info.isChampion || name.isEmpty()) return
		//val w2s = Renderer.worldToScreen(x, y, z)
		//textRenderer.color = Color.WHITE
		//textRenderer.draw(batch, data.name, w2s.first, w2s.second)
		//textRenderer.color = Color.YELLOW
		//textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
		val scale = 32F
		var xOff = scale * -2
		for (spell in spells) {
			val spellData = spell.data ?: continue
			val w2s = Renderer.worldToScreen(x, y, z - info.healthBarHeight)
			val tx = w2s.x + xOff
			val ty = w2s.y
			val ready = GameTime.gameTime >= spell.readyAt
			val lit = 0.8F
			val dimmed = lit / 2
			if (!ready) batch.setColor(dimmed, dimmed, dimmed, 1F) // dim
			batch.draw(spellData.loadIcon, tx - scale, ty, scale, scale, 0, 0, 64, 64, false, true)
			batch.setColor(lit, lit, lit, 1F)
			if (!ready) {
				val remaining = spell.readyAt - GameTime.gameTime
				textRenderer.color = Color.WHITE
				textRenderer.draw(
					batch,
					remaining.roundToInt().toString(),
					tx - scale + (scale / 4) - (scale / 10),
					ty + (scale / 2) + (scale / 10)
				)
			}
			xOff += scale
		}
		//textRenderer.draw(batch, "HP: ${health.toInt()}/${maxHealth.toInt()}", w2s.first, w2s.second + 20)
	}
	
	private fun runPlugins() {
		batch.begin()
		
		for (unit in Int2ObjectMaps.fastIterable(UnitManager.units))
			useUnit(unit.value)
		
		batch.end()
	}
	
	override fun render() {
		updateGameState()
		
		gl.apply {
			glEnable(GL20.GL_BLEND)
			glDisable(GL20.GL_DEPTH_TEST)
			glClearColor(0F, 0F, 0F, 0F)
			glClear(GL20.GL_COLOR_BUFFER_BIT)
			glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		}
		
		camera.setToOrtho(true, Screen.WIDTH.toFloat(), Screen.HEIGHT.toFloat())
		batch.projectionMatrix = camera.combined
		shapeRenderer.projectionMatrix = camera.combined
		
		runPlugins()
	}
	
	private fun createCheatComponents() {
		UnitInfo.load()
		SpellInfo.load()
		val hook = LeagueOfLegendsHook.hook()
		process = hook.process
		base = hook.baseModule
	}
	
	private fun createRenderComponents() {
		camera = OrthographicCamera(Screen.OVERLAY_WIDTH.toFloat(), Screen.OVERLAY_HEIGHT.toFloat()).apply {
			setToOrtho(true, Screen.OVERLAY_WIDTH.toFloat(), Screen.OVERLAY_HEIGHT.toFloat())
		}
		
		batch = SpriteBatch().apply { projectionMatrix = camera.combined }
		shapeRenderer = ShapeRenderer().apply { projectionMatrix = camera.combined; setAutoShapeType(true) }
		textRenderer = BitmapFont(true).apply { color = Color.RED }
	}
	
	private fun configureRendering() {
		gl.glClearColor(0F, 0F, 0F, 0F)
		myHWND = User32.FindWindowA(null, title)
		
		User32.SetWindowPos(
			myHWND,
			HWND_TOPPOS,
			Screen.OVERLAY_OFFSET,
			Screen.OVERLAY_OFFSET,
			Screen.OVERLAY_WIDTH,
			Screen.OVERLAY_HEIGHT,
			0
		)
		
		User32.SetForegroundWindow(myHWND)
		User32.SetActiveWindow(myHWND)
		User32.SetFocus(myHWND)
		
		OverlayManager.run {
			makeUndecorated(myHWND)
			makeTransparent(myHWND)
			makeClickthrough(myHWND)
			
			opened = true
		}
		
		created = true
	}
	
	override fun create() {
		createCheatComponents()
		createRenderComponents()
		configureRendering()
	}
	
	override fun dispose() {
		batch.dispose()
		shapeRenderer.dispose()
		textRenderer.dispose()
	}
	
	const val HWND_TOPPOS = -1L
	const val HWND_ZERO = 0L
	
}