plugins {
	idea
	kotlin("jvm")
}

val jnaVersion by extra("5.9.0")
val gdxVersion by extra("1.10.0")

val chronicle by extra("net.openhft:chronicle-core:2.21.91")
val fastutil by extra("it.unimi.dsi:fastutil:8.5.4")

allprojects {
	apply(plugin = "kotlin")
	
	group = "com.leagueofjire"
	version = "0.1.0"
	description = "League of Legends game interaction platform"
	
	repositories {
		mavenCentral()
	}
	
	java {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	
	tasks {
		compileKotlin {
			kotlinOptions.jvmTarget = "11"
		}
		compileTestKotlin {
			kotlinOptions.jvmTarget = "11"
		}
	}
}

subprojects {
	dependencies {
		implementation(rootProject)
	}
}