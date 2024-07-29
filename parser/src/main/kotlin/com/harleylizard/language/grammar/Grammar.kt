package com.harleylizard.language.grammar

import com.harleylizard.language.token.*
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
		val parameters = mutableListOf<Tree>()
		while (context.hasNext()) {
			when (context.token) {
				KeywordToken.FUNCTION -> parameters += function(context, asmify)
				KeywordToken.CLASS -> parameters += objectClass(context, asmify)
				KeywordToken.DATA -> parameters += dataClass(context, asmify)
				else -> context.skip()
			}
		}
		return ListTree(Collections.unmodifiableList(parameters))
	}

	private fun function(context: GrammarContext, asmify: Asmify): FunctionTree {
		context.expect(KeywordToken.FUNCTION)
		val name = context.identifier()

		context.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameters = parameters(context, asmify)

		var type = "V"
		if (context.maybeIs(OperatorToken.MINUS)) {
			context.expect(OperatorToken.GREATER_THAN)
			type = context.type(asmify)
		}
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		context.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

		return FunctionTree(Opcodes.ACC_PRIVATE, name, parameters, type, ListTree(emptyList()))
	}

	private fun parameters(context: GrammarContext, asmify: Asmify): ListTree<MemberTree> {
		val list = mutableListOf<MemberTree>()
		while (!context.get(SeparatorToken.CLOSE_ROUND_BRACKET)) {
			val name = context.identifier()
			context.expect(SeparatorToken.COLON)
			val type = context.type(asmify)
			list += MemberTree(name, type)

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

	private fun dataClass(context: GrammarContext, asmify: Asmify): DataClassTree {
		context.expect(KeywordToken.DATA)
		val className = asmify.asClass(context.identifier())
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)

		val fields = mutableListOf<MemberTree>()
		while (!context.get(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			val name = context.identifier()
			context.expect(SeparatorToken.COLON)
			val type = context.type(asmify)
			fields += MemberTree(name, type)
		}
		context.skip()
		return DataClassTree(className, ListTree(Collections.unmodifiableList(fields)))
	}
}