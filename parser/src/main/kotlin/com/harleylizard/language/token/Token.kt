package com.harleylizard.language.token

sealed interface Token {

	companion object {
		@JvmStatic
		fun get(value: String): Token {
			return when (value) {
				"import" -> KeywordToken.IMPORT
				"class" -> KeywordToken.CLASS
				"function" -> KeywordToken.FUNCTION
				"override" -> KeywordToken.OVERRIDE
				"var" -> KeywordToken.VARIABLE
				"array" -> KeywordToken.ARRAY
				"byte" -> KeywordToken.BYTE
				"short" -> KeywordToken.SHORT
				"int" -> KeywordToken.INT
				"long" -> KeywordToken.LONG
				"float" -> KeywordToken.FLOAT
				"double" -> KeywordToken.DOUBLE
				"+" -> OperatorToken.ADD
				"-" -> OperatorToken.SUBTRACT
				"*" -> OperatorToken.MULTIPLY
				"/" -> OperatorToken.DIVIDE
				"=" -> OperatorToken.EQUALS
				">" -> OperatorToken.GREATER_THAN
				"<" -> OperatorToken.LESS_THAN
				"(" -> SeparatorToken.OPEN_ROUND_BRACKET
				")" -> SeparatorToken.CLOSE_ROUND_BRACKET
				"{" -> SeparatorToken.OPEN_CURLY_BRACKET
				"}" -> SeparatorToken.CLOSE_CURLY_BRACKET
				"[" -> SeparatorToken.OPEN_SQUARE_BRACKET
				"]" -> SeparatorToken.CLOSE_SQUARE_BRACKET
				":" -> SeparatorToken.COLON
				"," -> SeparatorToken.COMMA
				else -> LiteralToken(value)
			}
		}
	}
}