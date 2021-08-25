package com.leagueofjire.scripts

abstract class Script {
	
	abstract fun ScriptContext.setup()

	open fun ScriptContext.run() {}
	
	open fun ScriptContext.teardown() {}
	
}