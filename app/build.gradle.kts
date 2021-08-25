dependencies {
	implementation(project(":api"))
	implementation(project(":core"))
	implementation(project(":scripts"))
	
	runtimeOnly(kotlin("script-runtime"))
	runtimeOnly(project(":userscripts"))
	
	implementation("io.github.classgraph:classgraph:4.8.115")
}