val gdxVersion: String by rootProject.extra
val fastutil: String by rootProject.extra

dependencies {
	compileOnly(project(":api"))
	compileOnly(project(":core"))
	
	compileOnly(kotlin("scripting-common"))
	
	compileOnly("com.badlogicgames.gdx:gdx:$gdxVersion")
	compileOnly(fastutil)
}