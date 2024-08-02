pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenLocal()
		mavenCentral()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "language") {
				useModule("com.harleylizard:gradle-plugin:1.0-SNAPSHOT")
			}
		}
	}
}
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "language"
include("parser")
include("examples")
includeBuild("gradle-plugin")
