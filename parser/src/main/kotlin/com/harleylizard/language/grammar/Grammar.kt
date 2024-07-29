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
		val asmify = Asmify(Collections.unmodifiableMap(map), context.gatherTemplates())
		val parameters = mutableListOf<Tree>()
		while (context.hasNext()) {
			when (context.token) {
				KeywordToken.FUNCTION -> parameters += function(context, asmify)
				KeywordToken.CLASS -> parameters += objectClass(context, asmify)
				KeywordToken.DATA -> parameters += dataClass(context, asmify)
				else -> context.skip()
			}
		}
		return ListTree.unmodifiable(parameters)
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

		return FunctionTree(Opcodes.ACC_PRIVATE, name, parameters, type, ListTree.empty())
	}

	private fun parameters(context: GrammarContext, asmify: Asmify): ListTree<MemberTree> {
		val list = mutableListOf<MemberTree>()
		while (!context.checkIf(SeparatorToken.CLOSE_ROUND_BRACKET)) {
			list += parameter(context, asmify)

			context.optional(SeparatorToken.COMMA)
		}
		context.skip()
		return ListTree.unmodifiable(list)
	}

	private fun objectClass(context: GrammarContext, asmify: Asmify): ObjectClassTree {
		context.expect(KeywordToken.CLASS)
		val name = asmify.asClass(context.identifier())
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)

		val list = mutableListOf<Tree>()
		while (!context.checkIf(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when {
				context.token == KeywordToken.FUNCTION -> list += function(context, asmify)
				else -> context.skip()
			}
		}
		context.skip()
		return ObjectClassTree(name, ListTree.unmodifiable(list))
	}

	private fun dataClass(context: GrammarContext, asmify: Asmify): DataClassTree {
		context.expect(KeywordToken.DATA)
		val className = asmify.asClass(context.identifier())
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)

		val fields = mutableListOf<MemberTree>()
		val operators = mutableListOf<FunctionTree>()
		while (!context.checkIf(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (context.token) {
				is IdentifierToken -> {
					fields += parameter(context, asmify)
				}
				KeywordToken.OPERATOR -> {
					operators += operatorFunction(context, asmify)
				}
				else -> context.skip()
			}

		}
		context.skip()
		return DataClassTree(className, ListTree.unmodifiable(fields), ListTree.unmodifiable(operators))
	}

	private fun operatorFunction(context: GrammarContext, asmify: Asmify): FunctionTree {
		context.expect(KeywordToken.OPERATOR)
		context.either(OperatorToken.ADD, OperatorToken.MINUS)
		val token = context.token
		val name = OperatorToken.overloadedName(token as OperatorToken)
		context.skip()

		context.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameter = parameter(context, asmify)
		context.expect(SeparatorToken.CLOSE_ROUND_BRACKET)

		val type = type(context, asmify)
		context.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		context.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

		return FunctionTree(Opcodes.ACC_PRIVATE, name, ListTree.singular(parameter), type, ListTree.empty())
	}

	private fun type(context: GrammarContext, asmify: Asmify): String {
		var type = "V"
		if (context.maybeIs(OperatorToken.MINUS)) {
			context.expect(OperatorToken.GREATER_THAN)
			type = context.type(asmify)
		}
		return type
	}

	private fun parameter(context: GrammarContext, asmify: Asmify): MemberTree {
		val name = context.identifier()
		context.expect(SeparatorToken.COLON)
		val type = context.type(asmify)
		return MemberTree(name, type)
	}
}