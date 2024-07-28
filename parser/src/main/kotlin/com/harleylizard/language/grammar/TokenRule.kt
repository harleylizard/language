package com.harleylizard.language.grammar

import com.harleylizard.language.token.Token

class TokenRule(private val token: Token) : Rule {

	override fun eval(context: Context) {
		if (context.matches(token)) {
			context.skip()
		} else {
			throw RuntimeException("expected $token, got ${context.currentToken}")
		}
	}
}