package com.harleylizard.language.tree

import com.harleylizard.language.token.*

class Grammar {

	fun parse(tokens: List<Token>): SyntaxTree {
		val iterator = TokenIterator(tokens)

		iterator.expect(KeywordToken.PACKAGE)
		val packageName = iterator.identifier()

		val classes = mutableListOf<ClassElement>()
		while (iterator.hasNext) {
			when (iterator.token) {
				KeywordToken.CLASS -> classes += regularClass(iterator)
				KeywordToken.DATA -> classes += dataClass(iterator)
				else -> iterator.skip()
			}
		}
		return SyntaxTree(packageName, emptyMap(), emptyMap(), ListElement.unmodifiable(classes))
	}

	private fun regularClass(iterator: TokenIterator): RegularClassElement {
		iterator.expect(KeywordToken.CLASS)
		val name = iterator.identifier()

		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		val functions = mutableListOf<FunctionElement>()

		while (!iterator.compare(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (iterator.token) {
				KeywordToken.FUNCTION -> functions += function(iterator)
				else -> iterator.skip()
			}
		}
		iterator.skip()
		return RegularClassElement(name, ListElement.unmodifiable(functions))
	}

	private fun function(iterator: TokenIterator): FunctionElement {
		iterator.expect(KeywordToken.FUNCTION)
		val name = iterator.identifier()

		iterator.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameters = mutableListOf<VariableElement>()

		while (!iterator.compare(SeparatorToken.CLOSE_ROUND_BRACKET)) {
			parameters += variable(iterator)
			iterator.maybeIs(SeparatorToken.COMMA)
		}
		iterator.skip()

		val type = returnType(iterator)
		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		iterator.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

		return FunctionElement(name, type, ListElement.unmodifiable(parameters))
	}

	private fun dataClass(iterator: TokenIterator): DataClassElement {
		iterator.expect(KeywordToken.DATA)
		val name = iterator.identifier()

		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		val fields = mutableListOf<VariableElement>()
		val operators = mutableListOf<FunctionElement>()

		while (!iterator.compare(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (iterator.token) {
				is IdentifierToken -> fields += variable(iterator)
				KeywordToken.OPERATOR -> operators += operatorFunction(iterator)
				else -> iterator.skip()
			}
		}
		iterator.skip()
		return DataClassElement(name, ListElement.unmodifiable(fields), ListElement.unmodifiable(operators))
	}

	private fun operatorFunction(iterator: TokenIterator): FunctionElement {
		iterator.expect(KeywordToken.OPERATOR)
		iterator.either(
			OperatorToken.ADD,
			OperatorToken.MINUS,
			OperatorToken.MULTIPLY,
			OperatorToken.DIVIDE
		)
		val name = (iterator.token as OperatorToken).asString
		iterator.skip()

		iterator.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameter = variable(iterator)
		iterator.expect(SeparatorToken.CLOSE_ROUND_BRACKET)

		val type = returnType(iterator)

		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		iterator.expect(SeparatorToken.CLOSE_CURLY_BRACKET)
		return FunctionElement(name, type, ListElement.singular(parameter))
	}

	private fun variable(iterator: TokenIterator): VariableElement {
		val name = iterator.identifier()
		iterator.expect(SeparatorToken.COLON)
		val type = type(iterator)
		return VariableElement(name, type)
	}

	private fun returnType(iterator: TokenIterator): String? {
		var type: String? = null
		if (iterator.maybeIs(OperatorToken.MINUS)) {
			iterator.expect(OperatorToken.GREATER_THAN)
			type = type(iterator)
		}
		return type
	}

	private fun type(iterator: TokenIterator): String {
		val type = iterator.token.asString
		iterator.skip()
		return type;
	}

	class TokenIterator(tokens: List<Token>) {
		private val iterator = tokens.iterator()
		var token = iterator.next(); private set

		val hasNext: Boolean; get() = token != EOFToken

		fun skip() {
			token = iterator.next()
		}

		fun either(vararg types: Token) {
			var match = false
			for (type in types) {
				if (token == type) {
					match = true
				}
			}
			if (!match) {
				val builder = StringBuilder()
				for (type in types) {
					builder.append("$type ")
				}
				val list = builder.toString()
				throw RuntimeException("expected one of $list" + "but got $token")
			}
		}

		fun expect(type: Token) {
			if (token != type) {
				throw RuntimeException("expected $type, got $token")
			}
			skip()
		}

		fun maybeIs(type: Token): Boolean {
			if (token == type) {
				skip()
				return true
			} else {
				return false
			}
		}

		fun identifier(): String {
			val copy = token
			if (copy is IdentifierToken) {
				skip()
				return copy.value
			}
			throw RuntimeException("expected identifier, got $token")
		}

		fun compare(type: Token) = token == type
	}
}