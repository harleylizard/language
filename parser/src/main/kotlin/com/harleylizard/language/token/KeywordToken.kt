package com.harleylizard.language.token

enum class KeywordToken : Token {
	PACKAGE,
	IMPORT,
	GENERIC,
	CLASS,
	DATA,
	INTERFACE,
	FUNCTION,
	OPERATOR,
	OVERRIDE,
	VARIABLE,
	ARRAY
	;

	override val asString: String
		get() = throw IllegalArgumentException("shouldn't parse keywords as identifiers")
}