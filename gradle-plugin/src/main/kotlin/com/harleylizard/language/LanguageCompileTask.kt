package com.harleylizard.language

import com.harleylizard.language.grammar.Grammar
import com.harleylizard.language.grammar.GrammarContext
import com.harleylizard.language.tree.ClassTree
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile
import org.objectweb.asm.ClassWriter
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

open class LanguageCompileTask : AbstractCompile() {

	@TaskAction
	fun compileLanguage() {
		source.files.forEach { file ->
			val tokens = Lexer.parse(readString(file))

			val context = GrammarContext(tokens)
			val list = Grammar().parse(context)

			for (entry in list) {
				if (entry is ClassTree) {
					val bytes = writeClass(entry)
					writeBytes(entry.name, bytes)

				}
			}
		}
	}

	private fun writeBytes(name: String, bytes: ByteArray) {
		ByteArrayOutputStream().use { byteArrayOf ->
			val path = Paths.get(destinationDirectory.asFile.get().path, "$name.class")
			path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

			Files.newOutputStream(path).use {
				byteArrayOf.writeBytes(bytes)
				byteArrayOf.writeTo(it)
			}
		}
	}

	private fun writeClass(classTree: ClassTree): ByteArray {
		val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
		classTree.asmify().accept(cw)
		return cw.toByteArray()
	}

	private fun readString(file: File): String {
		BufferedReader(InputStreamReader(file.inputStream())).use { reader ->
			val builder = StringBuilder()
			var line: String?
			while (reader.readLine().also { line = it } != null) {
				builder.append(line).append("\n")
			}

			return builder.toString()
		}
	}
}