package com.leagueofjire.scripts

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class DefaultOverlayContext(
	override val sprites: SpriteBatch,
	override val shapes: ShapeRenderer,
	override val font: BitmapFont
) : OverlayContext