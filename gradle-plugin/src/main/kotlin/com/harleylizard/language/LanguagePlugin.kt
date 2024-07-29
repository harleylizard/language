package com.harleylizard.language

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile

class LanguagePlugin : Plugin<Project> {

	override fun apply(target: Project) {
		target.pluginManager.apply(JavaPlugin::class.java)

		val compileTask = target.tasks.register("compileLanguage", LanguageCompileTask::class.java) { task ->
			task.destinationDirectory.set(target.layout.buildDirectory.dir("classes/language"))
			task.classpath = target.files(target.configurations.getByName("compileClasspath"))
		}

		target.extensions.getByType(JavaPluginExtension::class.java).sourceSets.all { sourceSet ->
			val directorySet = target.objects.sourceDirectorySet("language", "Language")
			directorySet.srcDir("src/${sourceSet.name}/language")
			directorySet.filter.include("**/*.language")
			//directorySet.compiledBy(compileTask, AbstractCompile::getDestinationDirectory)

			sourceSet.extensions.add("language", directorySet)
			sourceSet.allJava.source(directorySet)
			sourceSet.allSource.source(directorySet)

			compileTask.configure { task ->
				task.source(directorySet)
			}
		}

		target.tasks.withType(JavaCompile::class.java) { compile ->
			compile.dependsOn(compileTask)
		}
		target.tasks.withType(Jar::class.java) { jar ->
			jar.from(compileTask.get().destinationDirectory)
		}
	}
}