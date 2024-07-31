package com.harleylizard.language.token

import java.util.*

class Lexer {

	fun parse(source: String): List<Token> {
		val tokens = mutableListOf<Token>()

		Scanner(source.replace(regex, " $1 ")).use {
			while (it.hasNext()) {
				Token.get(it.next()).let(tokens::add)
			}
		}
		tokens.add(EOFToken)
		return Collections.unmodifiableList(tokens)
	}

	private companion object {

		@JvmStatic
		private val regex = Regex("([\\\\,{}()+&:;\\-*/=<>])")
	}
}