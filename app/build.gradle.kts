dependencies {
	implementation(project(":core"))
	implementation(project(":scripts"))
	
	runtimeOnly(kotlin("script-runtime"))
	runtimeOnly(project(":userscripts"))
}