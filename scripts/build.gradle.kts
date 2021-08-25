dependencies {
	implementation(project(":api"))
	implementation(project(":core"))
	
	implementation(kotlin("scripting-common"))
	runtimeOnly(kotlin("script-runtime"))
}