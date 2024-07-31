package com.harleylizard.language.tree

import com.harleylizard.language.token.SeparatorToken

data class ParameterElement(
	val name: String,
	val type: String
) : Element {

	companion object {

		@JvmStatic
		fun parameter(iterator: Grammar.TokenIterator): ParameterElement {
			val name = iterator.identifier()
			iterator.expect(SeparatorToken.COLON)
			val type = iterator.type()
			return ParameterElement(name, type)
		}
	}
}