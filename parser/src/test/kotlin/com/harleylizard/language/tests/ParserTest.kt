package com.harleylizard.language.tests

import com.harleylizard.language.Lexer
import com.harleylizard.language.Parser
import org.junit.jupiter.api.Test

class ParserTest {

	@Test
	fun test() {
		val tokens = Lexer.parse(Resources.readString("test.language"))
		val tree = Parser(tokens.iterator()).parse()
	}
}