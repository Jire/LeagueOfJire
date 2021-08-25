dependencies {
	implementation(project(":core"))
	
	implementation(kotlin("scripting-common"))
	runtimeOnly(kotlin("script-runtime"))
}