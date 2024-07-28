package com.harleylizard.language.grammar

import com.harleylizard.language.token.IdentifierToken

class IdentifierRule : Rule {
	override fun eval(context: Context) {
		if (context.currentToken is IdentifierToken) {
			context.skip()
		} else {
			throw RuntimeException("expected identifier, got ${context.currentToken}")
		}
	}
}