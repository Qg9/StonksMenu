plugins {
    kotlin("jvm") version "2.1.21"
    id("java-library")
    id("maven-publish")
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://dependency.download/releases")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.imanity.dev/imanity-libraries/")
        maven("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation(project(":Common"))
    implementation(project(":V21"))
    implementation(project(":V8"))
}

kotlin {
    jvmToolchain(21)
}
