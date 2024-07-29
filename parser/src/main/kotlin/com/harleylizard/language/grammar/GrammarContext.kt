package com.harleylizard.language.grammar

import com.harleylizard.language.Template
import com.harleylizard.language.token.*
import com.harleylizard.language.tree.Asmify
import java.util.*

class GrammarContext(private val tokens: List<Token>) {
	private val iterator = tokens.iterator()

	var token = iterator.next(); private set

	fun skip() {
		token = iterator.next()
	}

	fun expect(token: Token) {
		this.token.takeIf { it == token }?.let { skip() } ?: throw RuntimeException("expected $token, got ${this.token}")
	}

	fun either(vararg tokens: Token) {
		var result = false
		for (token in tokens) {
			if (this.token == token) {
				result = true
			}
		}
		if (!result) {
			val builder = StringBuilder()
			for (token in tokens) {
				builder.append("$token ")
			}
			throw RuntimeException("expected one of $token" + "but got ${this.token}")
		}
	}

	fun optional(token: Token) {
		this.token.takeIf { it == token }?.let { skip() }
	}

	fun identifier(): String {
		return token.takeIf { it is IdentifierToken }?.let {
			skip()
			return (it as IdentifierToken).value
		} ?: throw RuntimeException("expected identifier, got $token")
	}

	fun type(asmify: Asmify): String {
		if (token == KeywordToken.ARRAY) {
			skip()
			expect(OperatorToken.LESS_THAN)
			val type = type(asmify)
			expect(OperatorToken.GREATER_THAN)
			return "[$type"
		}
		val type = when (token) {
			KeywordToken.BYTE -> "B"
			KeywordToken.SHORT -> "S"
			KeywordToken.INT -> "I"
			KeywordToken.LONG -> "J"
			KeywordToken.FLOAT -> "F"
			KeywordToken.DOUBLE -> "D"
			is IdentifierToken -> (token as IdentifierToken).value
			else -> "V"
		}
		skip()
		return asmify.asDescriptor(type)
	}

	fun hasNext() = token != EOFToken

	fun checkIf(token: Token): Boolean {
		return token.takeIf { this.token == it }?.let {
			return true
		} ?: false
	}

	fun maybeIs(token: Token): Boolean {
		if (this.token == token) {
			skip()
			return true
		}
		return false
	}

	fun gatherImports(packageName: String): List<Pair<String, String>> {
		val list = mutableListOf<Pair<String, String>>()
		val context = GrammarContext(tokens)
		while (context.hasNext()) {
			when (context.token) {
				KeywordToken.IMPORT -> {
					context.expect(KeywordToken.IMPORT)
					val name = context.identifier()
					list += Pair(name.substring(name.lastIndexOf(".") + 1, name.length), name.replace(".", "/"))
				}
				KeywordToken.CLASS, KeywordToken.DATA -> {
					context.skip()
					val name = context.identifier()
					list += Pair(name, "$packageName/$name")
				}
				else -> context.skip()
			}
		}
		return Collections.unmodifiableList(list)
	}

	fun gatherTemplates(): Map<String, Template> {
		val map = mutableMapOf<String, Template>()
		val context = GrammarContext(tokens)
		while (context.hasNext()) {
			if (context.checkIf(KeywordToken.TEMPLATE)) {
				context.expect(KeywordToken.TEMPLATE)
				val name = context.identifier()
				var type: String? = null
				if (context.checkIf(SeparatorToken.COLON)) {
					context.expect(SeparatorToken.COLON)
					type = context.identifier()
				}
				map[name] = Template(type)
			} else {
				context.skip()
			}
		}
		return Collections.unmodifiableMap(map)
	}
}