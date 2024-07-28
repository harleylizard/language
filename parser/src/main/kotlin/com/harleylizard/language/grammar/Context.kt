package com.harleylizard.language.grammar

import com.harleylizard.language.token.Token

class Context(private val tokens: Iterator<Token>) {
	private var token = tokens.next()

	val currentToken; get() = token

	fun skip() {
		token = tokens.next()
	}

	fun matches(token: Token) = this.token == token
}