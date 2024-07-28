package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import com.harleylizard.language.Parser
import com.harleylizard.language.tree.ClassTree
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassWriter
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths

class AsmifyTest {

	@Test
	fun test() {
		val tokens = Lexer.parse(Resources.readString("test.language"))
		val list = Parser(tokens.iterator()).parse()

		for (tree in list) {
			if (tree is ClassTree) {
				val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
				tree.asmify().accept(cw)

				ByteArrayOutputStream().use { byteArrayOf ->
					val path = Paths.get("build/language/", "${tree.name}.class")
					path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

					Files.newOutputStream(path).use {
						byteArrayOf.writeBytes(cw.toByteArray())
						byteArrayOf.writeTo(it)
					}
				}
			}
		}
	}
}