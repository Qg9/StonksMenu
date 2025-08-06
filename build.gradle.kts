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
    implementation(project(path = ":Common", configuration = "default"))
    implementation(project(path = ":V21", configuration = "default"))
    implementation(project(path = ":V8", configuration = "default"))
}

kotlin {
    jvmToolchain(21)
}

configurations.all {
    attributes.attribute(
        org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.attribute,
        org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.jvm
    )
}
