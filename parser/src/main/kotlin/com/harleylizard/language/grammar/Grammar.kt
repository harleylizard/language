package com.harleylizard.language.grammar

import com.harleylizard.language.token.KeywordToken
import com.harleylizard.language.token.SeparatorToken
import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import java.util.*

class Grammar {

	fun parse(context: GrammarContext): ListTree<Tree> {
		context.expect(KeywordToken.PACKAGE)
		val packageName = context.identifier().replace(".", "/")

		val map = mutableMapOf<String, String>()
		for (import in context.gatherImports(packageName)) {
			map[import.first] = import.second
		}
		val asmify = Asmify(Collections.unmodifiableMap(map))
		val list = mutableListOf<Tree>()
		while (context.hasNext()) {
			when (context.token) {
				KeywordToken.FUNCTION -> list += function(context, asmify)
				KeywordToken.CLASS -> list += objectClass(context, asmify)
				else -> context.skip()
			}
		}
		return ListTree(Collections.unmodifiableList(list))
	}

	private fun function(context: GrammarContext, asmify: Asmify): FunctionTree {
		context.expect(KeywordToken.FUNCTION)
		val name = context.identifier()

		context.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameters = parameters(context, asmify)

		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		context.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

		return FunctionTree(Opcodes.ACC_PRIVATE, name, parameters, "V", ListTree(emptyList()))
	}

	private fun parameters(context: GrammarContext, asmify: Asmify): ListTree<ParameterTree> {
		val list = mutableListOf<ParameterTree>()
		while (!context.get(SeparatorToken.CLOSE_ROUND_BRACKET)) {
			val name = context.identifier()
			context.expect(SeparatorToken.COLON)
			val type = context.type(asmify)
			list += ParameterTree(name, type)

			context.optional(SeparatorToken.COMMA)
		}
		context.skip()
		return ListTree(Collections.unmodifiableList(list))
	}

	private fun objectClass(context: GrammarContext, asmify: Asmify): ObjectClassTree {
		context.expect(KeywordToken.CLASS)
		val name = asmify.asClass(context.identifier())
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)

		val list = mutableListOf<Tree>()
		while (!context.get(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when {
				context.token == KeywordToken.FUNCTION -> list += function(context, asmify)
				else -> context.skip()
			}
		}
		context.skip()
		return ObjectClassTree(name, ListTree(Collections.unmodifiableList(list)))
	}
}