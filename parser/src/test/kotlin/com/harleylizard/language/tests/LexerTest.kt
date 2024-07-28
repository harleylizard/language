package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import org.junit.jupiter.api.Test

class LexerTest {

	@Test
	fun test() {
		val tokens = Lexer.parse("function foo() {}")
	}
}