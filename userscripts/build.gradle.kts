dependencies {
	implementation(project(":api"))
	implementation(project(":core"))
	implementation(project(":scripts"))
}

sourceSets.main {
	java.srcDirs("scripts")
}