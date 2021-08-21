plugins {
	kotlin("jvm") version "1.5.10"
	java
}

group = "org.jire.leagueofjire"
version = "0.1.0"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	
	implementation("net.java.dev.jna:jna:5.8.0")
	implementation("net.java.dev.jna:jna-platform:5.8.0")
	
	implementation("org.jire", "kna", "0.4.2")
	
	implementation("it.unimi.dsi:fastutil:8.5.4")
	
	implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
}