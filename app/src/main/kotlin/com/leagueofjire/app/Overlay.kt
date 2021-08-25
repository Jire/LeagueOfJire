package com.leagueofjire.app

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.leagueofjire.app.transparency.*
import com.leagueofjire.game.*
import com.leagueofjire.core.native.User32
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.jire.kna.JNAPointerCache
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.windows.User32.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread
import kotlin.math.min

class Overlay(val title: String = ThreadLocalRandom.current().nextLong().toString()) : ApplicationAdapter() {
	
	var myHWND = -1L
	
	lateinit var sprites: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapes: ShapeRenderer
	lateinit var texts: BitmapFont
	
	private val renderHooks: ObjectList<Overlay.() -> Unit> = ObjectArrayList()
	
	fun renderHook(body: Overlay.() -> Unit) {
		renderHooks.add(body)
	}
	
	override fun render() {
		for (i in 0..renderHooks.lastIndex)
			renderHooks[i]()
	}
	
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
			myHWND, HWND_TOPMOST,
			Screen.OVERLAY_OFFSET, Screen.OVERLAY_OFFSET,
			Screen.OVERLAY_WIDTH, Screen.OVERLAY_HEIGHT,
			0
		)
		
		User32.SetForegroundWindow(myHWND)
		User32.SetActiveWindow(myHWND)
		User32.SetFocus(myHWND)
		
		makeUndecorated(myHWND)
		makeTransparent(myHWND)
		makeClickthrough(myHWND)
		
		if (false) thread {
			val gameHWND = 12L//Pointer.nativeValue(window.pointer)
			val overlayHWND = myHWND
			
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
	
	override fun create() {
		createRenderComponents()
		configureRendering()
		openHook()
	}
	
	override fun dispose() {
		sprites.dispose()
		shapes.dispose()
		texts.dispose()
	}
	
	private lateinit var openHook: Overlay.() -> Unit
	
	fun open(openHook: Overlay.() -> Unit): Lwjgl3Application {
		this.openHook = openHook
		val config = Lwjgl3ApplicationConfiguration().apply {
			setTitle(title)
			setWindowPosition(0, 0)
			setWindowedMode(Screen.WIDTH, Screen.HEIGHT)
			useOpenGL3(true, 4, 6)
			setResizable(false)
			setDecorated(false)
			useVsync(false)
			disableAudio(true)
			
			GLFW.glfwSwapInterval(0)
			GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE)
			
			val antialiasingSamples = 0//4
			// note: we don't need any bits for the back buffer besides just 1 for alpha
			setBackBufferConfig(0, 0, 0, 1, 0, 0, antialiasingSamples)
			
			val fps = 60
			setForegroundFPS(fps)
			setIdleFPS(min(30, fps))
		}
		return Lwjgl3Application(this, config)
	}
	
	fun makeTransparent(myHWND: Long) = TransparencyUser32.SetWindowCompositionAttribute(
		myHWND,
		WindowCompositionAttributeData(
			AccentState = AccentStates.ACCENT_ENABLE_TRANSPARENTGRADIENT,
			AccentFlags = AccentFlags.Transparent
		)
	)
	
	fun makeUndecorated(myHWND: Long) = User32.SetWindowLongA(
		myHWND,
		GWL_EXSTYLE,
		WS_EX_COMPOSITED or WS_EX_LAYERED or WS_EX_TRANSPARENT or WS_EX_TOOLWINDOW or WS_EX_TOPMOST
	)
	
	fun makeClickthrough(myHWND: Long) = User32.SetWindowLongA(
		myHWND,
		WinUser.GWL_STYLE,
		User32.GetWindowLongA(myHWND, WinUser.GWL_STYLE) and WinUser.WS_OVERLAPPEDWINDOW.inv()
	)
	
	init {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	}
	
}