pluginManagement {
	val kotlinVersion by extra("1.5.30")
	
	plugins {
		kotlin("jvm") version kotlinVersion
	}
	
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

rootProject.name = "leagueofjire"

include("api")
include("core")

include("scripts")
include("userscripts")

include("app")