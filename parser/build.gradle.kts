plugins {
    `maven-publish`
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

dependencies {
    api("org.ow2.asm:asm:9.7")
    api("org.ow2.asm:asm-commons:9.7")
    api("org.ow2.asm:asm-util:9.7")
    api("org.ow2.asm:asm-tree:9.7")
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "parser"
            version = project.version as String
            from(components["java"])
        }
    }
}

