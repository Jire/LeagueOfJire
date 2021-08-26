plugins {
	application
}

val jnaVersion: String by rootProject.extra
val gdxVersion: String by rootProject.extra
val fastutil: String by rootProject.extra

dependencies {
	implementation(project(":api"))
	implementation(project(":core"))
	implementation(project(":scripts"))
	
	runtimeOnly(kotlin("script-runtime"))
	runtimeOnly(project(":userscripts"))
	
	implementation("net.java.dev.jna:jna:$jnaVersion")
	implementation("net.java.dev.jna:jna-platform:$jnaVersion")
	
	implementation("org.jire:kna:0.4.2")
	
	implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
	
	implementation(fastutil)
	
	implementation("io.github.classgraph:classgraph:4.8.115")
}

application {
	mainClass.set("com.leagueofjire.app.LeagueOfJire")
	/*applicationDefaultJvmArgs = listOf("-agentpath:\"C:\\Program Files\\YourKit Java Profiler 2021.3-b231\\bin\\win64\\yjpagent.dll\")*/
}