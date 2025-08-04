plugins {
    kotlin("jvm") version "2.1.21"
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.qg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dependency.download/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.imanity.dev/imanity-libraries/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.imanity.paperspigot:paper1.8.8:1.8.8")

    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("com.ezylang:EvalEx:3.5.0")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(8)
}