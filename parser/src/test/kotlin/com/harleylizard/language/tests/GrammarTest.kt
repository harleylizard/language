package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import com.harleylizard.language.grammar.grammar
import com.harleylizard.language.token.KeywordToken
import com.harleylizard.language.token.SeparatorToken
import org.junit.jupiter.api.Test

class GrammarTest {

	@Test
	fun test() {
		val tokens = Lexer.parse(Resources.readString("test.language"))
		grammar {
			val function = group {
				token(KeywordToken.FUNCTION) then
				identifier() then
				token(SeparatorToken.OPEN_ROUND_BRACKET)
			}
			KeywordToken.FUNCTION map function

		}.validity(tokens)
	}
}
