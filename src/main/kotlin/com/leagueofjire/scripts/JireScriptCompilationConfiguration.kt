package com.leagueofjire.scripts

import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

object JireScriptCompilationConfiguration : ScriptCompilationConfiguration({
	baseClass(JireScript::class)
	defaultImports(
		"com.leagueofjire.game.*",
		"com.leagueofjire.input.*",
		"com.leagueofjire.overlay.Overlay",
		"com.leagueofjire.overlay.Screen"
	)
	//implicitReceivers(ScriptHelper::class)
	ide {
		acceptedLocations(ScriptAcceptedLocation.Everywhere)
	}
	jvm {
		dependenciesFromClassloader(classLoader = JireScript::class.java.classLoader, wholeClasspath = true)
	}
})