package com.harleylizard.language

import com.harleylizard.language.asmify.Acceptor
import com.harleylizard.language.asmify.Asmify
import com.harleylizard.language.token.Lexer
import com.harleylizard.language.tree.*
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.*
import java.nio.file.*

open class LanguageCompileTask : AbstractCompile() {

	@TaskAction
	fun compileLanguage() {
		destinationDirectory.asFileTree.files.forEach { file ->
			file.delete()
		}
		source.files.forEach { file ->
			val sourcePath = file.path
			val sourceName = sourcePath.substring(sourcePath.lastIndexOf("\\") + 1, sourcePath.indexOf("."))

			val tokens = Lexer().parse(readString(file))
			val syntaxTree = Grammar().parse(tokens)

			val functions = mutableListOf<FunctionElement>()

			for (element in syntaxTree.elements) {
				if (element is FunctionElement) {
					functions += element
					continue
				}
				if (element is Acceptor && element is Named) {
					write(sourceName, element, syntaxTree)
				}
			}

			if (functions.isNotEmpty()) {
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
				val asmify = Asmify.immutableAsmify(sourceName, cw, syntaxTree)

				val qualifier = asmify.qualifier(sourceName)
				val node = ClassNode()
				node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, qualifier, null, "java/lang/Object", null)

				for (element in functions) {
					element.accepts(node, asmify)
				}
				node.accept(cw)
				cw.visitEnd()
				write(cw, pathOf(sourceName, asmify))
			}
		}
	}

	private fun <T> write(sourceName: String, element: T, syntaxTree: SyntaxTree) where T : Acceptor, T : Named {
		val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
		val asmify = Asmify.immutableAsmify(sourceName, cw, syntaxTree)

		element.accepts(asmify)
		write(cw, pathOf(element.name, asmify))
	}

	private fun write(cw: ClassWriter, path: Path) {
		ByteArrayOutputStream().use { byteArrayOf ->
			Files.newOutputStream(path).use {
				byteArrayOf.writeBytes(cw.toByteArray())
				byteArrayOf.writeTo(it)
			}
		}
	}

	private fun pathOf(name: String, asmify: Asmify): Path {
		val dest = destinationDirectory.get().toString()

		val path = Paths.get(dest, "${asmify.qualifier(name)}.class")
		path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)
		return path
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