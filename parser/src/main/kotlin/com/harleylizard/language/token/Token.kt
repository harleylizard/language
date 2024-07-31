package com.harleylizard.language.token

sealed interface Token {
	val asString: String

	companion object {

		@JvmStatic
		fun get(value: String): Token {
			return when (value) {
				"package" -> KeywordToken.PACKAGE
				"import" -> KeywordToken.IMPORT
				"generic" -> KeywordToken.GENERIC
				"class" -> KeywordToken.CLASS
				"data" -> KeywordToken.DATA
				"interface" -> KeywordToken.INTERFACE
				"trait" -> KeywordToken.TRAIT
				"function" -> KeywordToken.FUNCTION
				"operator" -> KeywordToken.OPERATOR
				"override" -> KeywordToken.OVERRIDE
				"var" -> KeywordToken.VARIABLE
				"array" -> KeywordToken.ARRAY
				"set" -> KeywordToken.SET
				"byte" -> TypeToken.BYTE
				"short" -> TypeToken.SHORT
				"int" -> TypeToken.INT
				"long" -> TypeToken.LONG
				"float" -> TypeToken.FLOAT
				"double" -> TypeToken.DOUBLE
				"+" -> OperatorToken.ADD
				"-" -> OperatorToken.MINUS
				"*" -> OperatorToken.MULTIPLY
				"/" -> OperatorToken.DIVIDE
				"=" -> OperatorToken.EQUALS
				">" -> OperatorToken.GREATER_THAN
				"<" -> OperatorToken.LESS_THAN
				"&" -> OperatorToken.BITWISE_AND
				"(" -> SeparatorToken.OPEN_ROUND_BRACKET
				")" -> SeparatorToken.CLOSE_ROUND_BRACKET
				"{" -> SeparatorToken.OPEN_CURLY_BRACKET
				"}" -> SeparatorToken.CLOSE_CURLY_BRACKET
				"[" -> SeparatorToken.OPEN_SQUARE_BRACKET
				"]" -> SeparatorToken.CLOSE_SQUARE_BRACKET
				":" -> SeparatorToken.COLON
				";" -> SeparatorToken.SEMI_COLON
				"," -> SeparatorToken.COMMA
				else -> IdentifierToken(value)
			}
		}
	}
}