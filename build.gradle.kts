/*
 * LeagueOfJire: Free & Open-Source External Scripting Platform
 * Copyright (C) 2021  Thomas G. P. Nappo (Jire)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	version = "1.0.0"
	description = "Free & Open-Source External Scripting Platform"
	
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