package com.leagueofjire.scripts

import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

object JireScriptCompilationConfiguration : ScriptCompilationConfiguration({
	defaultImports(
		"com.leagueofjire.game.*",
		"com.leagueofjire.input.*",
		"com.leagueofjire.input.Keyboard.keyPressed",
		"com.leagueofjire.overlay.Overlay",
		"com.leagueofjire.overlay.Screen"
	)
	implicitReceivers(ScriptContext::class)
	implicitReceivers(HelperBro::class)
	ide {
		acceptedLocations(ScriptAcceptedLocation.Everywhere)
	}
	jvm {
		dependenciesFromClassloader(classLoader = JireScript::class.java.classLoader, wholeClasspath = true)
	}
})