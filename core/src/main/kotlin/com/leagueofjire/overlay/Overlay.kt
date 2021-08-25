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
import com.leagueofjire.overlay.transparency.AccentPolicy
import com.leagueofjire.overlay.transparency.WindowCompositionAttributeData
import com.leagueofjire.scripts.JireScriptCompilationConfiguration
import com.leagueofjire.scripts.JireScriptEvaluationConfiguration
import com.leagueofjire.scripts.ScriptContext
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.jire.kna.JNAPointerCache
import org.jire.kna.PointerCache
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.lwjgl.system.windows.User32.HWND_TOPMOST
import java.io.File
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object Overlay : ApplicationAdapter() {
	
	val title = ThreadLocalRandom.current().nextLong().toString()
	
	@Volatile
	var created: Boolean = false
	
	lateinit var sprites: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapes: ShapeRenderer
	lateinit var texts: BitmapFont
	
	lateinit var window: WinDef.HWND
	lateinit var process: AttachedProcess
	lateinit var base: AttachedModule
	
	lateinit var scriptContext: ScriptContext
	
	private val bodies: ObjectList<Unit.() -> kotlin.Unit> = ObjectArrayList()
	
	operator fun invoke(body: Unit.() -> kotlin.Unit) {
		bodies.add(body)
	}
	
	override fun render() {
		if (!scriptContext.update() || GameTime.gameTime < 5F) return
		ScreenUtils.clear(0F, 0F, 0F, 0F)
		scriptContext.render()
	}
	
	private fun createCheatComponents() {
		UnitInfo.load()
		SpellInfo.load()
		
		val hook = LeagueOfLegendsHook.hook()
		window = hook.window
		process = hook.process
		base = hook.baseModule
		
		scriptContext = loadScriptContext()
	}
	
	fun loadScriptContext() = ScriptContext(Overlay, GameTime, Renderer, Minimap, UnitManager, LocalPlayer, HoveredUnit)
		.apply { load() }
	
	private fun createRenderComponents() {
		val vw = Screen.OVERLAY_WIDTH.toFloat()
		val vy = Screen.OVERLAY_HEIGHT.toFloat()
		camera = OrthographicCamera(vw, vy).apply { setToOrtho(true, vw, vy) }
		
		sprites = SpriteBatch().apply { projectionMatrix = camera.combined }
		shapes = ShapeRenderer().apply { projectionMatrix = camera.combined; setAutoShapeType(true) }
		texts = BitmapFont(true).apply { color = Color.RED }
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
			
			val gameHWND = Pointer.nativeValue(window.pointer)
			val overlayHWND = myHWND
			
			if (false) {
				thread {
					var needsApply = true
					while (!Thread.interrupted()) {
						val foreground = User32.GetForegroundWindow()
						if (foreground == gameHWND) {
							if (false && !needsApply) continue
							needsApply = false
							val pol = AccentPolicy().apply {
								AccentState = 3
								AccentFlags = 2
								GradientColor = 0
								AnimationId = 0
							}
							val data = WindowCompositionAttributeData().apply {
								Attribute = 19
								Data = pol.pointer
								SizeOfData = pol.size()
							}
							//User32.SetWindowPos(gameHWND, myHWND, 0, 0, 0, 0, 2 or 1)
							//User32.SetWindowCompositionAttribute(gameHWND, data)
							
							User32.SetWindowPos(
								myHWND,
								HWND_TOPMOST,
								Screen.OVERLAY_OFFSET,
								Screen.OVERLAY_OFFSET,
								Screen.OVERLAY_WIDTH,
								Screen.OVERLAY_HEIGHT,
								0
							)
							val hwnd3 = User32.GetWindow(JNAPointerCache[foreground], WinUser.GW_HWNDPREV)
							//User32.SetWindowPos(myHWND, Pointer.nativeValue(hwnd3), 0, 0, 0, 0, 2 or 1)
							User32.SetWindowPos(Pointer.nativeValue(hwnd3), myHWND, 0, 0, 0, 0, 2 or 1)
							//makeTransparent(gameHWND)
							println("APPLIED! $foreground / $gameHWND / $overlayHWND")
						} else {
							needsApply = true
							println("NOT FOREGROUND! $foreground / $gameHWND / $overlayHWND")
						}
						Thread.sleep(200L)
					}
				}
			}
			
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
		sprites.dispose()
		shapes.dispose()
		texts.dispose()
	}
	
	const val HWND_TOPPOS = -1L
	const val HWND_ZERO = 0L
	
}