package com.harleylizard.language

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

class LanguagePlugin : Plugin<Project> {

	override fun apply(target: Project) {
		target.pluginManager.apply(JavaPlugin::class.java)

		target.extensions.getByType(JavaPluginExtension::class.java).sourceSets.all {
			val directorySet = target.objects.sourceDirectorySet("language", "Language")
			directorySet.srcDir("src/${it.name}/language")
			directorySet.filter.include("**/*.language")

			it.extensions.add("language", directorySet)
			it.allJava.source(directorySet)
			it.allSource.source(directorySet)
		}
	}
}