plugins {
    kotlin("jvm") version "2.1.21"
}

group = "fr.qg.menu.v21"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(project(":common"))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}