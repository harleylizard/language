package com.harleylizard.language.grammar

import com.harleylizard.language.token.EOFToken
import com.harleylizard.language.token.Token

fun grammar(unit: Builder.() -> Unit): Grammar {
	val builder = Builder()
	builder.unit()
	return builder.build()
}

class Grammar(private val rule: Rule) {

	fun validity(tokens: List<Token>) {
		rule.eval(Context(tokens.iterator()))
	}
}

class Builder {
	private val map = mutableMapOf<Token, Rule>()

	infix fun Token.map(rule: Rule) {
		map[this] = rule
	}

	fun group(unit: () -> Rule): Rule {
		return Rule { context ->
			unit().eval(context)
		}
	}

	fun token(token: Token) = TokenRule(token)

	fun skip() = SkipRule()

	fun identifier() = IdentifierRule()

	fun build(): Grammar {
		return Grammar { context ->
			while (context.currentToken != EOFToken) {
				when {
					map.containsKey(context.currentToken) -> map[context.currentToken]?.eval(context)
					else -> context.skip()
				}
			}
		}
	}
}