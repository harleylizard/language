package com.harleylizard.language

import com.harleylizard.language.token.Token
import java.util.*

class Lexer {

	fun parse(input: String) : List<Token> {
		val tokens = mutableListOf<Token>()

		val scanner = Scanner(input.replace(spliterator, " $1 "))
		while (scanner.hasNext()) {
			Token.get(scanner.next())?.let { tokens += it }
		}
		return Collections.unmodifiableList(tokens)
	}

	private companion object {
		@JvmStatic
		private val spliterator = Regex("([\\\\,{}()+:\\-*/=<>])")

	}
}