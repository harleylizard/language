package com.harleylizard.language.token

sealed interface Token {

	companion object {
		@JvmStatic
		fun get(value: String): Token {
			return when (value) {
				"package" -> KeywordToken.PACKAGE
				"import" -> KeywordToken.IMPORT
				"template" -> KeywordToken.TEMPLATE
				"class" -> KeywordToken.CLASS
				"data" -> KeywordToken.DATA
				"function" -> KeywordToken.FUNCTION
				"operator" -> KeywordToken.OPERATOR
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
				"," -> SeparatorToken.COMMA
				else -> IdentifierToken(value)
			}
		}
	}
}