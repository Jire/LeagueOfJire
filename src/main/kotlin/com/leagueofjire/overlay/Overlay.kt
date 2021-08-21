package com.leagueofjire.overlay

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.leagueofjire.native.User32
import com.leagueofjire.overlay.OverlayManager.myHWND
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.concurrent.ThreadLocalRandom

object Overlay : ApplicationAdapter() {
	
	val title = ThreadLocalRandom.current().nextLong().toString()
	
	@Volatile
	var created: Boolean = false
	
	lateinit var batch: SpriteBatch
	lateinit var camera: OrthographicCamera
	lateinit var shapeRenderer: ShapeRenderer
	lateinit var textRenderer: BitmapFont
	
	private val bodies: ObjectList<Overlay.() -> Unit> = ObjectArrayList()
	
	override fun create() {
		camera = OrthographicCamera(Screen.OVERLAY_WIDTH.toFloat(), Screen.OVERLAY_HEIGHT.toFloat()).apply {
			setToOrtho(true, Screen.OVERLAY_WIDTH.toFloat(), Screen.OVERLAY_HEIGHT.toFloat())
		}
		
		batch = SpriteBatch().apply { projectionMatrix = camera.combined }
		shapeRenderer = ShapeRenderer().apply { setAutoShapeType(true) }
		textRenderer = BitmapFont(true).apply { color = Color.RED }
		
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
	
	override fun render() {
		gl.apply {
			glEnable(GL20.GL_BLEND)
			glDisable(GL20.GL_DEPTH_TEST)
			glClearColor(0F, 0F, 0F, 0F)
			glClear(GL20.GL_COLOR_BUFFER_BIT)
			glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
			
			camera.setToOrtho(true, Screen.WIDTH.toFloat(), Screen.HEIGHT.toFloat())
			batch.projectionMatrix = camera.combined
			shapeRenderer.projectionMatrix = camera.combined

/*			shapeRenderer.run {
				begin(ShapeRenderer.ShapeType.Line)
				color = Color.MAGENTA
				rect(200F, 200F, 200F, 200F)
				end()
			}*/
			
			for (i in 0..bodies.size - 1) bodies[i]()
			
			glFinish()
			/*val aimData = AimBotState.aimData
			if (aimData != 0L) {
				val xLow = (aimData ushr 48) and 0xFFFF
				val xHigh = (aimData ushr 32) and 0xFFFF
				val yLow = (aimData ushr 16) and 0xFFFF
				val yHigh = aimData and 0xFFFF
				val rectWidth = (xHigh - xLow)
				val rectHeight = (yHigh - yLow)
				shapeRenderer.run {
					begin(ShapeRenderer.ShapeType.Line)
					color = Color.MAGENTA
					rect(
						(xLow + captureOffsetX).toFloat(),
						(yLow + captureOffsetY).toFloat(),
						rectWidth.toFloat(),
						rectHeight.toFloat()
					)
					end()
				}
			}*/
		}
	}
	
	operator fun invoke(body: Overlay.() -> Unit) {
		bodies.add(body)
	}
	
	override fun dispose() {
		batch.dispose()
		shapeRenderer.dispose()
		textRenderer.dispose()
	}
	
	const val HWND_TOPPOS = -1L
	const val HWND_ZERO = 0L
	
}