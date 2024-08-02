package com.harleylizard.language.tests

import com.harleylizard.language.asmify.Acceptor
import com.harleylizard.language.asmify.Asmify
import com.harleylizard.language.token.Lexer
import com.harleylizard.language.tree.*
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.ByteArrayOutputStream
import java.nio.file.*

class AsmifyTest {

	@Test
	fun test() {
		val sourcePath = "Test.language"
		val sourceName = sourcePath.substring(0, sourcePath.indexOf("."))
		val tokens = Lexer().parse(Resources.readString(sourcePath))
		val syntaxTree = Grammar().parse(tokens)

		val functions = mutableListOf<FunctionElement>()

		for (element in syntaxTree.elements) {
			if (element is FunctionElement) {
				functions += element
				continue
			}
			if (element is Acceptor && element is Named) {
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
				val asmify = Asmify.immutableAsmify(sourceName, cw, syntaxTree)

				element.accepts(asmify)
				write(cw, pathOf(element.name, asmify))
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

	private fun write(cw: ClassWriter, path: Path) {
		ByteArrayOutputStream().use { byteArrayOf ->
			Files.newOutputStream(path).use {
				byteArrayOf.writeBytes(cw.toByteArray())
				byteArrayOf.writeTo(it)
			}
		}
	}

	private fun pathOf(name: String, asmify: Asmify): Path {
		val path = Paths.get("build/classes/language/", "${asmify.qualifier(name)}.class")
		path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)
		return path
	}
}