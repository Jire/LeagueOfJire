val gdxVersion: String by rootProject.extra
val fastutil: String by rootProject.extra
val chronicle: String by rootProject.extra
val jnaVersion: String by rootProject.extra

dependencies {
	compileOnly(project(":api"))
	
	compileOnly("com.badlogicgames.gdx:gdx:$gdxVersion")
	
	compileOnly(fastutil)
	
	compileOnly("net.java.dev.jna:jna:$jnaVersion")
	compileOnly("net.java.dev.jna:jna-platform:$jnaVersion")
	
	compileOnly("org.jire:kna:0.4.2")
	
	val jacksonVersion = "2.12.4"
	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
	
	compileOnly(chronicle)
}