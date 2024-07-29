package com.harleylizard.language.tests

// class Parser(private val tokens: Iterator<Token>) {
// 	private var token = tokens.next()
//
// 	fun parse(): List<Tree> {
// 		val tree = mutableListOf<Tree>()
// 		while (token != EOFToken) {
// 			when (token) {
// 				KeywordToken.FUNCTION -> tree += parseFunction()
// 				KeywordToken.CLASS -> tree += parseClass()
// 				KeywordToken.IMPORT -> tree += parseImport()
// 				KeywordToken.PACKAGE -> tree += parsePackage()
// 				KeywordToken.DATA -> tree += parseData()
// 				else -> token = tokens.next()
// 			}
// 		}
// 		return Collections.unmodifiableList(tree)
// 	}
//
// 	private fun parseLambdaParameter() {
// 		val parameters = mutableListOf<ParameterTree>()
// 		while (token != SeparatorToken.CLOSE_ROUND_BRACKET) {
// 			parameters += parseType()
// 		}
// 	}
//
// 	private fun parseType(): ParameterTree {
// 		val name = nextLiteral()
// 		next(SeparatorToken.COLON)
// 		if (next(SeparatorToken.OPEN_ROUND_BRACKET)) {
// 			parseLambdaParameter()
// 			return ParameterTree(name, "V", false)
// 		}
// 		val type = nextType()
// 		val reference = next(OperatorToken.BITWISE_AND)
// 		return ParameterTree(name, type, reference)
// 	}
//
// 	private fun parseData(): DataTree {
// 		next(KeywordToken.DATA)
// 		val name = nextLiteral()
// 		next(SeparatorToken.OPEN_CURLY_BRACKET)
//
// 		val parameters = mutableListOf<ParameterTree>()
// 		while (token != SeparatorToken.CLOSE_CURLY_BRACKET) {
// 			val paramName = nextLiteral()
// 			next(SeparatorToken.COLON)
// 			parameters += ParameterTree(paramName, nextType(), false)
// 		}
// 		token = tokens.next()
//
// 		return DataTree(name, Collections.unmodifiableList(parameters))
// 	}
//
// 	private fun parsePackage(): PackageTree {
// 		next(KeywordToken.PACKAGE)
// 		val literal = nextLiteral()
// 		return PackageTree(literal.replace(".", "/"))
// 	}
//
// 	private fun parseParameters(): List<ParameterTree> {
// 		val parameters = mutableListOf<ParameterTree>()
// 		while (token != SeparatorToken.CLOSE_ROUND_BRACKET) {
// 			parameters += parseType()
//
// 			next(SeparatorToken.COMMA)
// 		}
// 		return Collections.unmodifiableList(parameters)
// 	}
//
// 	private fun parseFunction(): FunctionTree {
// 		next(KeywordToken.FUNCTION)
// 		val name = nextLiteral()
// 		next(SeparatorToken.OPEN_ROUND_BRACKET)
// 		val parameters = parseParameters()
// 		token = tokens.next()
//
// 		var type = "V"
// 		if (nextArrow()) {
// 			type = nextType()
// 		}
// 		next(SeparatorToken.OPEN_CURLY_BRACKET)
// 		next(SeparatorToken.CLOSE_CURLY_BRACKET)
//
// 		return FunctionTree(Opcodes.ACC_PRIVATE, name, parameters, type, ListTree(emptyList()))
// 	}
//
// 	private fun parseClass(): SubClassTree {
// 		next(KeywordToken.CLASS)
// 		val name = nextLiteral()
// 		next(SeparatorToken.OPEN_CURLY_BRACKET)
//
// 		next(SeparatorToken.OPEN_CURLY_BRACKET)
// 		val list = mutableListOf<Tree>()
// 		while (token != SeparatorToken.CLOSE_CURLY_BRACKET) {
// 			when {
// 				token == KeywordToken.FUNCTION -> list += parseFunction()
// 				else -> token = tokens.next()
// 			}
// 		}
//
// 		return SubClassTree(name, ListTree(Collections.unmodifiableList(list)))
// 	}
//
// 	private fun parseImport(): ImportTree {
// 		next(KeywordToken.IMPORT)
// 		val literal = nextLiteral()
// 		return ImportTree(literal.substring(literal.lastIndexOf(".") + 1, literal.length), literal.replace(".", "/"))
// 	}
//
// 	private fun <T : Token> next(t: T): Boolean {
// 		if (token == t) {
// 			token = tokens.next()
// 			return true
// 		}
// 		return false
// 	}
//
// 	private fun nextLiteral(): String {
// 		if (token is IdentifierToken) {
// 			val value = (token as IdentifierToken).value
// 			token = tokens.next()
// 			return value
// 		}
// 		token = tokens.next()
// 		return ""
// 	}
//
// 	private fun nextType(): String {
// 		if (token == KeywordToken.ARRAY) {
// 			token = tokens.next()
// 			next(OperatorToken.LESS_THAN)
// 			val l = nextType()
// 			print(l)
// 			next(OperatorToken.GREATER_THAN)
// 			return "[$l"
// 		}
// 		val klass = when (token) {
// 			KeywordToken.BYTE -> "B"
// 			KeywordToken.SHORT -> "S"
// 			KeywordToken.INT -> "I"
// 			KeywordToken.LONG -> "J"
// 			KeywordToken.FLOAT -> "F"
// 			KeywordToken.DOUBLE -> "D"
// 			is IdentifierToken -> (token as IdentifierToken).value
// 			else -> "V"
// 		}
// 		token = tokens.next()
// 		return klass
// 	}
//
// 	private fun nextArrow(): Boolean {
// 		if (token == OperatorToken.SUBTRACT) {
// 			token = tokens.next()
// 			if (token == OperatorToken.GREATER_THAN) {
// 				token = tokens.next()
// 				return true
// 			}
// 		}
// 		return false
// 	}
// }