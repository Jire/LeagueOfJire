plugins {
	kotlin("jvm") version "1.5.10"
	java
}

group = "com.leagueofjire"
version = "0.1.0"

val jnaVersion = "5.8.0"
val gdxVersion = "1.10.0"
val visuiVersion = "1.4.7"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	
	implementation("net.java.dev.jna:jna:$")
	implementation("net.java.dev.jna:jna-platform:$jnaVersion")
	
	implementation("org.jire", "kna", "0.4.2")
	
	implementation("it.unimi.dsi:fastutil:8.5.4")
	
	implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
	
	implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
	
	implementation("net.openhft:chronicle-core:2.20.127.1")
}