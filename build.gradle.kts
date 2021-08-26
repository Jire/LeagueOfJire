plugins {
	kotlin("jvm") version "1.5.30"
	application
}

val jnaVersion = "5.8.0"
val gdxVersion = "1.10.0"
val visuiVersion = "1.4.7"

application {
	mainClass.set("com.leagueofjire.app.LeagueOfJire")
	applicationDefaultJvmArgs = listOf(
		"-Xmx4g",
		"-Xms4g"
		/*, "-agentpath:\"C:\\Program Files\\YourKit Java Profiler 2021.3-b231\\bin\\win64\\yjpagent.dll\""*/
	)
}

dependencies {
	runtimeOnly(project(":app"))
}

allprojects {
	group = "com.leagueofjire"
	version = "0.1.0"
	
	apply {
		plugin("org.jetbrains.kotlin.jvm")
	}
	
	repositories {
		mavenCentral()
	}
	
	dependencies {
		implementation(kotlin("stdlib", "1.5.30"))
		
		implementation("org.jire", "kna", "0.4.2") {
			exclude(module = "kotlin-stdlib")
			exclude(module = "kotlin-stdlib-jdk8")
			exclude(module = "kotlin-reflect")
		}
		
		implementation("net.java.dev.jna:jna:$jnaVersion")
		implementation("net.java.dev.jna:jna-platform:$jnaVersion")
		
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
		
		implementation("net.openhft:chronicle-core:2.21.91")
	}
	
	java {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	
	tasks {
		compileKotlin {
			kotlinOptions {
				jvmTarget = "11"
			}
		}
	}
}