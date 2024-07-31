package com.harleylizard.language.tree

import com.harleylizard.language.token.*

class Grammar {

	fun parse(tokens: List<Token>): SyntaxTree {
		val iterator = TokenIterator(tokens)

		iterator.expect(KeywordToken.PACKAGE)
		val packageName = iterator.identifier()

		val elements = mutableListOf<Element>()
		while (iterator.hasNext) {
			when (iterator.token) {
				KeywordToken.INTERFACE -> elements += InterfaceParser.jnterface(iterator)
				KeywordToken.CLASS -> elements += ClassParser.klass(iterator)
				KeywordToken.DATA -> elements += DataParser.data(iterator)
				KeywordToken.TRAIT -> elements += TraitParser.trait(iterator)
				else -> iterator.skip()
			}
		}
		return SyntaxTree(packageName, emptyMap(), emptyMap(), ListElement.unmodifiable(elements))
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

		fun skipIf(type: Token): Boolean {
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

		fun type(): String {
			if (skipIf(KeywordToken.ARRAY)) {
				expect(OperatorToken.LESS_THAN)
				val type = type()
				expect(OperatorToken.GREATER_THAN)
				return "[$type"
			}
			val type = token.asString
			skip()
			return type;
		}

		fun returnType(): String? {
			var type: String? = null
			if (equalTo(OperatorToken.MINUS)) {
				expect(OperatorToken.GREATER_THAN)
				type = type()
			}
			return type
		}

		fun equalTo(type: Token) = token == type
	}
}