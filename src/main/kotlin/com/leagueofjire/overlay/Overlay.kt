package com.leagueofjire.overlay

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.leagueofjire.game.*
import com.leagueofjire.game.Unit
import com.leagueofjire.native.User32
import com.leagueofjire.overlay.OverlayManager.myHWND
import com.leagueofjire.scripts.JireScriptCompilationConfiguration
import com.leagueofjire.scripts.JireScriptEvaluationConfiguration
import com.leagueofjire.scripts.cdtracker.CooldownTracker
import com.sun.jna.platform.win32.WinDef
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.lwjgl.system.windows.User32.HWND_TOPMOST
import java.io.File
import java.util.concurrent.ThreadLocalRandom
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object Overlay : ApplicationAdapter() {
	
	val title = ThreadLocalRandom.current().nextLong().toString()
	
	@Volatile
	var created: Boolean = false
	
	lateinit var batch: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapeRenderer: ShapeRenderer
	lateinit var textRenderer: BitmapFont
	
	lateinit var window: WinDef.HWND
	lateinit var process: AttachedProcess
	lateinit var base: AttachedModule
	
	fun loadScripts() {
		if (true) {
			CooldownTracker.init()
			return
		}
		val files = File("scripts").listFiles()!!
		for (file in files) evalFile(file)
	}
	
	fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
		return BasicJvmScriptingHost().eval(
			scriptFile.readText().toScriptSource(),
			JireScriptCompilationConfiguration,
			JireScriptEvaluationConfiguration
		)
	}
	
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
	
	private val bodies: ObjectList<Unit.() -> kotlin.Unit> = ObjectArrayList()
	
	operator fun invoke(body: Unit.() -> kotlin.Unit) {
		bodies.add(body)
	}
	
	private fun runPlugins() {
		batch.begin()
		
		for (entry in Int2ObjectMaps.fastIterable(UnitManager.units)) {
			val unit = entry.value
			for (i in 0..bodies.lastIndex)
				bodies[i](unit)
		}
		
		batch.end()
	}
	
	override fun render() {
		updateGameState()
		
		ScreenUtils.clear(0F, 0F, 0F, 0F)
		
		runPlugins()
	}
	
	private fun createCheatComponents() {
		loadScripts()
		
		UnitInfo.load()
		SpellInfo.load()
		
		val hook = LeagueOfLegendsHook.hook()
		window = hook.window
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
			HWND_TOPMOST,
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
			
			//makeTransparent(Pointer.nativeValue(window.pointer))
			
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