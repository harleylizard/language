package com.harleylizard.language.token

enum class OperatorToken(private val overloadName: String) : Token {
	MINUS("minus"),
	ADD("add"),
	DIVIDE("divide"),
	MULTIPLY("multiply"),
	EQUALS("equals"),
	GREATER_THAN("greaterThan"),
	LESS_THAN("lessThan"),
	BITWISE_AND("bitwiseAnd")
	;

	override val asString: String get() = overloadName
}