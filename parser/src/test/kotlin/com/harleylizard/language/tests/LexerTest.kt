package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import org.junit.jupiter.api.Test

class LexerTest {

	@Test
	fun test() {
		val lexer = Lexer()
		val tokens = lexer.parse("func foo(j: int) { var i = 0 }")
	}
}