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
    application
}

val slf4jSimple: String by rootProject.extra
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

    implementation("io.github.classgraph:classgraph:4.8.153")

    implementation(slf4jSimple)
}

application {
    mainClass.set("com.leagueofjire.app.LeagueOfJire")
    applicationDefaultJvmArgs += arrayOf(
        "-Xmx4g",
        "-Xms1g",

        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+UseZGC",

        "--enable-native-access=ALL-UNNAMED",

        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.time=ALL-UNNAMED",

        "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED"
    )
}
