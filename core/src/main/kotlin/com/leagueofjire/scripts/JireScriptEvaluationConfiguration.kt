package com.leagueofjire.scripts

import com.leagueofjire.overlay.Overlay
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.implicitReceivers

object JireScriptEvaluationConfiguration : ScriptEvaluationConfiguration({
	implicitReceivers(Overlay.loadScriptContext())
})