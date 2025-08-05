plugins {
    kotlin("jvm") version "2.1.21"
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation(project(":Common"))
    implementation(project(":V21"))
    implementation(project(":V8"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("kotlin", "fr.qg.menu.utils.kotlin")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}

kotlin {
    jvmToolchain(21)
}
