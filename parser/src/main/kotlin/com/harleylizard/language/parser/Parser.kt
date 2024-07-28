package com.harleylizard.language.parser

import com.harleylizard.language.token.LiteralToken
import com.harleylizard.language.token.Token

sealed interface Parser {
	val tokens: Iterator<Token>
	var token: Token

	fun next(token: Token) = token.takeIf { this.token == it }?.let {
		this.token = tokens.next()
		return true
	} ?: false

	fun identifier() = token.takeIf { it is LiteralToken }?.let {
		return (token as LiteralToken).value
	} ?: ""

	fun skip() {
		token = tokens.next()
	}
}