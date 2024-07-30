package com.harleylizard.language.tests

import com.harleylizard.language.asmify.Asmify
import com.harleylizard.language.token.Lexer
import com.harleylizard.language.tree.Grammar
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassWriter
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths

class AsmifyTest {

	@Test
	fun test() {
		val tokens = Lexer().parse(Resources.readString("test.language"))
		val syntaxTree = Grammar().parse(tokens)

		val asmify = Asmify.create(syntaxTree)

		for (clazz in syntaxTree.classes) {
			val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
			val node = asmify.asmify(clazz)
			node.accept(cw)

			ByteArrayOutputStream().use { of ->
				val path = Paths.get("build/classes/language/", "${clazz.name}.class")
				path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

				Files.newOutputStream(path).use {
					of.writeBytes(cw.toByteArray())
					of.writeTo(it)
				}
			}
		}
	}
}