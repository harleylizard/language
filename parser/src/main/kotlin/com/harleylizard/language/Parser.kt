package com.harleylizard.language

import com.harleylizard.language.token.*
import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import java.util.*

class Parser(private val tokens: Iterator<Token>) {
	private var token = tokens.next()

	fun parse(): List<Tree> {
		val tree = mutableListOf<Tree>()
		while (token != EOFToken) {
			when (token) {
				KeywordToken.FUNCTION -> tree += parseFunction()
				KeywordToken.CLASS -> tree += parseClass()
				else -> token = tokens.next()
			}
		}
		return Collections.unmodifiableList(tree)
	}

	private fun parseParameters(): List<ParameterTree> {
		val parameters = mutableListOf<ParameterTree>()
		while (token != SeparatorToken.CLOSE_ROUND_BRACKET) {
			val name = nextLiteral()
			next(SeparatorToken.COLON)
			parameters += ParameterTree(name, nextType())

			if (token == SeparatorToken.COMMA) {
				next(SeparatorToken.COMMA)
			}
		}
		return Collections.unmodifiableList(parameters)
	}

	private fun parseFunction(): FunctionTree {
		next(KeywordToken.FUNCTION)
		val name = nextLiteral()
		next(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameters = parseParameters()
		token = tokens.next()

		var type = "void"
		if (nextArrow()) {
			type = nextType()
		}
		next(SeparatorToken.OPEN_CURLY_BRACKET)
		next(SeparatorToken.CLOSE_CURLY_BRACKET)

		return FunctionTree(Opcodes.ACC_PUBLIC, name, parameters, type, ListTree(emptyList()))
	}

	private fun parseClass(): ClassTree {
		next(KeywordToken.CLASS)
		val name = nextLiteral()
		next(SeparatorToken.OPEN_CURLY_BRACKET)

		next(SeparatorToken.OPEN_CURLY_BRACKET)
		val list = mutableListOf<Tree>()
		while (token != SeparatorToken.CLOSE_CURLY_BRACKET) {
			when {
				token == KeywordToken.FUNCTION -> list += parseFunction()
				else -> token = tokens.next()
			}
		}

		return ClassTree(name, ListTree(Collections.unmodifiableList(list)))
	}

	private fun <T : Token> next(t: T) {
		if (token == t) {
			token = tokens.next()
		}
	}

	private fun nextLiteral(): String {
		if (token is LiteralToken) {
			val value = (token as LiteralToken).value
			token = tokens.next()
			return value
		}
		token = tokens.next()
		return ""
	}

	private fun nextType(): String {
		val klass = when (token) {
			KeywordToken.BYTE -> "byte"
			KeywordToken.SHORT -> "short"
			KeywordToken.INT -> "int"
			KeywordToken.LONG -> "long"
			KeywordToken.FLOAT -> "float"
			KeywordToken.DOUBLE -> "double"
			is LiteralToken -> (token as LiteralToken).value
			else -> "unknown"
		}
		token = tokens.next()
		return klass
	}

	private fun nextArrow(): Boolean {
		if (token == OperatorToken.SUBTRACT) {
			token = tokens.next()
			if (token == OperatorToken.GREATER_THAN) {
				token = tokens.next()
				return true
			}
		}
		return false
	}
}