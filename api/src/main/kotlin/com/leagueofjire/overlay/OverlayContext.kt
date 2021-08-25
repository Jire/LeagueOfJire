package com.leagueofjire.overlay

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface OverlayContext {
	
	val sprites: SpriteBatch
	val shapes: ShapeRenderer
	val font: BitmapFont
	
}