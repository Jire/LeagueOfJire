dependencies {
	implementation(project(":api"))
	implementation(project(":scripts"))
}

sourceSets.main {
	java.srcDirs("scripts")
}