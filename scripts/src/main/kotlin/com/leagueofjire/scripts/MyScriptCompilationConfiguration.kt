package com.leagueofjire.scripts

import kotlin.script.experimental.api.*

internal object MyScriptCompilationConfiguration : ScriptCompilationConfiguration({
	defaultImports(
		"java.awt.event.KeyEvent",
		"com.badlogic.gdx.graphics.Color",
		"com.leagueofjire.ScreenPosition",
		"com.leagueofjire.util.*",
		"com.leagueofjire.input.*",
		"com.leagueofjire.overlay.*",
		"com.leagueofjire.game.*",
		"com.leagueofjire.game.unit.*",
		"com.leagueofjire.game.unit.champion.*",
		"com.leagueofjire.game.unit.champion.item.*",
		"com.leagueofjire.game.unit.champion.spell.*",
		"com.leagueofjire.game.unit.champion.spell.GameChampionSpells.*",
	)
	ide {
		acceptedLocations(ScriptAcceptedLocation.Everywhere)
	}
})