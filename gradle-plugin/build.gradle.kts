plugins {
    kotlin("jvm") version "2.0.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api("com.harleylizard:parser:1.0-SNAPSHOT")
    //api(project("**/language/parser"))
}

gradlePlugin {
    plugins {
        val language by creating {
            id = "language"
            implementationClass = "com.harleylizard.language.LanguagePlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "language"
            version = project.version as String
            from(components["java"])
        }
    }
}

tasks.build {
    finalizedBy(tasks.publishToMavenLocal)
}