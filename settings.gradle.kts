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

pluginManagement {
	val kotlinVersion by extra("1.8.0")
	
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
