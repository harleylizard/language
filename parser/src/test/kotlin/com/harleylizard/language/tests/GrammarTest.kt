package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import com.harleylizard.language.grammar.Grammar
import com.harleylizard.language.grammar.GrammarContext
import org.junit.jupiter.api.Test

class GrammarTest {

	@Test
	fun test() {
		val tokens = Lexer.parse(Resources.readString("test.language"))
		val grammar = Grammar()
		grammar.parse(GrammarContext(tokens))
	}
}