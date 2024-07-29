package com.harleylizard.language.old

import com.harleylizard.language.token.IdentifierToken
import com.harleylizard.language.token.Token

sealed interface Parser {
	val tokens: Iterator<Token>
	var token: Token

	fun next(token: Token) = token.takeIf { this.token == it }?.let {
		this.token = tokens.next()
		return true
	} ?: false

	fun identifier() = token.takeIf { it is IdentifierToken }?.let {
		return (token as IdentifierToken).value
	} ?: ""

	fun skip() {
		token = tokens.next()
	}
}