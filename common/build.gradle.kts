plugins {
    kotlin("jvm") version "2.1.21"
}

group = "fr.qg"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("com.ezylang:EvalEx:3.5.0")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}