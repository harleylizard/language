package com.harleylizard.language.token

enum class OperatorToken : Token {
	MINUS, ADD, DIVIDE, MULTIPLY, EQUALS, GREATER_THAN, LESS_THAN, BITWISE_AND;

	companion object {

		@JvmStatic
		fun overloadedName(token: OperatorToken): String {
			return when (token) {
				MINUS -> "minus"
				ADD -> "add"
				DIVIDE -> "divide"
				MULTIPLY -> "multiply"
				EQUALS -> "assign"
				GREATER_THAN -> "greaterThan"
				LESS_THAN -> "lessThan"
				BITWISE_AND -> "bitwiseAnd"
			}
		}
	}
}