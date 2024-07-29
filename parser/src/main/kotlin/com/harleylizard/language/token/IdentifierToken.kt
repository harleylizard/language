package com.harleylizard.language.token

@JvmInline
value class IdentifierToken(val value: String) : Token {
	override val asString: String; get() = value

}
