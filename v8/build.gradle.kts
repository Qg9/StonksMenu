plugins {
    kotlin("jvm") version "2.1.21"
}

group = "fr.qg.menu.v21"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
    maven("https://dependency.download/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.imanity.dev/imanity-libraries/")
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly(project(":common"))
    compileOnly("org.imanity.paperspigot:paper1.8.8:1.8.8")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}