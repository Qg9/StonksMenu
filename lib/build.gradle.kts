plugins {
    kotlin("jvm") version "2.1.21"
    `java-library`
    `maven-publish`
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
    implementation(project(":v21"))
    implementation(project(":v8"))
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("jitpack") {
            from(components["kotlin"]) // ou "kotlin" si lib est Kotlin pur
            groupId = "com.github.Qg9"
            artifactId = "StonksMenu"
            version = "1.0.0"
        }
    }
}
