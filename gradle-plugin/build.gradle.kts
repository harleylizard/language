plugins {
    `java-gradle-plugin`
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        val language by creating {
            id = "language"
            implementationClass = "com.harleylizard.language.LanguagePlugin"
        }
    }
}