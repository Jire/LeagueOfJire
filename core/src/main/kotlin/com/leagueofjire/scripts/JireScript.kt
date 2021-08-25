package com.leagueofjire.scripts

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
	"LeagueOfJire Script",
	fileExtension = "jire.kts",
	compilationConfiguration = JireScriptCompilationConfiguration::class,
	evaluationConfiguration = JireScriptEvaluationConfiguration::class
)
abstract class JireScript {
	
	fun bro() {
		println("please work")
	}
	
}