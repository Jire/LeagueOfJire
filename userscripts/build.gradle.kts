val gdxVersion: String by rootProject.extra
val fastutil: String by rootProject.extra

dependencies {
	compileOnly(project(":api"))
	compileOnly(project(":scripts"))
	
	compileOnly("com.badlogicgames.gdx:gdx:$gdxVersion")
	compileOnly(fastutil)
}

sourceSets.main {
	java.srcDirs("scripts")
}