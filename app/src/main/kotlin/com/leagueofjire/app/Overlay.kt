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

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.leagueofjire.app.transparency.AccentFlags
import com.leagueofjire.app.transparency.AccentStates
import com.leagueofjire.app.transparency.TransparencyUser32
import com.leagueofjire.app.transparency.WindowCompositionAttributeData
import com.leagueofjire.core.game.IGameContext
import com.leagueofjire.core.native.User32
import com.sun.jna.platform.win32.WinUser
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.windows.User32.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

class Overlay(
	val gameContext: IGameContext,
	val title: String = ThreadLocalRandom.current().nextLong().toString()
) : ApplicationAdapter() {
	
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
		myHWND = User32.FindWindowA(null, title)

		User32.SetForegroundWindow(myHWND)
		User32.SetActiveWindow(myHWND)
		User32.SetFocus(myHWND)
		
		makeUndecorated(myHWND)
		makeTransparent(myHWND)
		makeClickthrough(myHWND)
		
		User32.SetWindowPos(
			myHWND, HWND_TOPMOST,
			Screen.OVERLAY_OFFSET, Screen.OVERLAY_OFFSET,
			Screen.OVERLAY_WIDTH, Screen.OVERLAY_HEIGHT,
			2 or 1
		)
	}
	
	override fun create() {
		Thread.currentThread().apply { name = "LeagueOfJire"; priority = Thread.MAX_PRIORITY }
		
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
