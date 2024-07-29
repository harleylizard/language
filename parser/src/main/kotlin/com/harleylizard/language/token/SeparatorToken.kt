package com.harleylizard.language.token

enum class SeparatorToken : Token {
	OPEN_ROUND_BRACKET,
	CLOSE_ROUND_BRACKET,
	OPEN_CURLY_BRACKET,
	CLOSE_CURLY_BRACKET,
	OPEN_SQUARE_BRACKET,
	CLOSE_SQUARE_BRACKET,
	COLON,
	COMMA
	;

	override val asString: String
		get() = throw IllegalArgumentException("shouldn't parse separators as identifiers")
}