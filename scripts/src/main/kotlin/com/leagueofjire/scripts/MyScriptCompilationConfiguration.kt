package com.leagueofjire.scripts

import kotlin.script.experimental.api.*

object MyScriptCompilationConfiguration : ScriptCompilationConfiguration({
	defaultImports(
		"com.leagueofjire.game.*",
		"com.leagueofjire.input.*",
		"com.leagueofjire.util.*",
		"com.leagueofjire.overlay.Screen",
		"com.badlogic.gdx.graphics.Color",
		"java.awt.event.KeyEvent",
	)
	ide {
		acceptedLocations(ScriptAcceptedLocation.Everywhere)
	}
})