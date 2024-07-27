plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")
}

allprojects {
    dependencies {
        testImplementation(kotlin("test"))
        implementation("it.unimi.dsi:fastutil:8.5.13")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(19)
    }
}