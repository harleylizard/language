package com.harleylizard.language.tests

import com.harleylizard.language.asmify.Acceptor
import com.harleylizard.language.asmify.Asmify
import com.harleylizard.language.token.Lexer
import com.harleylizard.language.tree.Grammar
import com.harleylizard.language.tree.Named
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

		for (element in syntaxTree.elements) {
			if (element is Acceptor && element is Named) {
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
				val asmify = Asmify.immutableAsmify(cw, syntaxTree)

				element.accepts(asmify)
				ByteArrayOutputStream().use { of ->
					val path = Paths.get("build/classes/language/", "${asmify.qualifier(element.name)}.class")
					path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

					Files.newOutputStream(path).use {
						of.writeBytes(cw.toByteArray())
						of.writeTo(it)
					}
				}
			}
		}
	}
}