package com.leagueofjire.overlay

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.leagueofjire.LeagueOfJire
import com.leagueofjire.native.User32
import com.leagueofjire.overlay.transparency.AccentFlags
import com.leagueofjire.overlay.transparency.AccentStates
import com.leagueofjire.overlay.transparency.WindowCompositionAttributeData
import com.sun.jna.platform.win32.WinUser
import org.lwjgl.glfw.GLFW
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread
import kotlin.math.min
import org.lwjgl.system.windows.User32 as LWJGLUser32

object OverlayManager {
	
	@Volatile
	var opened = false
	
	@Volatile
	var myHWND = -1L
	
	fun open() {
		val overlay = Overlay
		val config = Lwjgl3ApplicationConfiguration().apply {
			setTitle(overlay.title)
			setWindowPosition(0, 0)
			setWindowedMode(Screen.WIDTH, Screen.HEIGHT)
			useOpenGL3(true, 4, 6)
			setResizable(false)
			setDecorated(false)
			useVsync(false)
			GLFW.glfwSwapInterval(0)
			GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE)
			//setBackBufferConfig(8, 8, 8, 8, 16, 0, 0) // samples 4
			
			val fps = 0/*60*/
			setForegroundFPS(fps)
			setIdleFPS(min(30, fps))
		}
		thread(name = "LeagueOfJire", priority = Thread.MAX_PRIORITY) {
			Lwjgl3Application(overlay, config)
		}
	}
	
	fun makeTransparent(myHWND: Long) = User32.SetWindowCompositionAttribute(
		myHWND,
		WindowCompositionAttributeData(
			AccentState = AccentStates.ACCENT_ENABLE_TRANSPARENTGRADIENT, AccentFlags = AccentFlags.Transparent
		)
	)
	
	fun makeUndecorated(myHWND: Long) = User32.SetWindowLongA(
		myHWND,
		LWJGLUser32.GWL_EXSTYLE,
		LWJGLUser32.WS_EX_COMPOSITED or
				LWJGLUser32.WS_EX_LAYERED or
				LWJGLUser32.WS_EX_TRANSPARENT or
				LWJGLUser32.WS_EX_TOOLWINDOW or LWJGLUser32.WS_EX_TOPMOST
	)
	
	fun makeClickthrough(myHWND: Long) = User32.SetWindowLongA(
		myHWND,
		WinUser.GWL_STYLE,
		User32.GetWindowLongA(myHWND, WinUser.GWL_STYLE)
				and WinUser.WS_OVERLAPPEDWINDOW.inv()
	)
	
	init {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	}
	
}