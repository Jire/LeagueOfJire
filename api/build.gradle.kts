val gdxVersion: String by rootProject.extra
val fastutil: String by rootProject.extra

dependencies {
	compileOnly(fastutil)
	compileOnly("com.badlogicgames.gdx:gdx:$gdxVersion")
}