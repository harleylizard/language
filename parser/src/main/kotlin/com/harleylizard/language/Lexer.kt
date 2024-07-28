package com.harleylizard.language

import com.harleylizard.language.token.EOFToken
import com.harleylizard.language.token.Token
import java.util.*

object Lexer {
	@JvmStatic
	private val regex = Regex("([\\\\,{}()+&:\\-*/=<>])")

	@JvmStatic
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
}