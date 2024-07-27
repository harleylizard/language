package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import org.junit.jupiter.api.Test

class LexerTest {

	@Test
	fun test() {
		Lexer().parse("fn foo(j: int) { var i = 0 }")

	}
}