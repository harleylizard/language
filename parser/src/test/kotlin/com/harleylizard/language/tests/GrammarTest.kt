package com.harleylizard.language.tests

import com.harleylizard.language.token.Lexer
import com.harleylizard.language.tree.Grammar
import org.junit.jupiter.api.Test

class GrammarTest {

	@Test
	fun test() {
		val lexer = Lexer()
		val grammar = Grammar()

		val tokens = lexer.parse(Resources.readString("Test.language"))
		val syntaxTree = grammar.parse(tokens)

		println(syntaxTree.packageName)
	}
}