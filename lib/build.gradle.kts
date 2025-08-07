import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.21"
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.0"
}

repositories {
    mavenCentral()
    maven("https://dependency.download/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.imanity.dev/imanity-libraries/")
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    implementation(project(":common"))
    implementation(project(":v8"))
    implementation(project(":v21"))
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    mergeServiceFiles {
        path = "META-INF/services"
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("jitpack") {
            groupId = "com.github.Qg9"
            artifactId = "StonksMenu"
            version = "1.0.0"

            val shadowJar = tasks.named<ShadowJar>("shadowJar").get()
            artifact(shadowJar.archiveFile) {
                builtBy(shadowJar)
            }

            //artifact(sourcesJar)
        }
    }
}