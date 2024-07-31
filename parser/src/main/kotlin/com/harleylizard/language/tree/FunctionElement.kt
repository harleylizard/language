package com.harleylizard.language.tree

import com.harleylizard.language.token.KeywordToken
import com.harleylizard.language.token.SeparatorToken

data class FunctionElement(
	val name: String,
	val type: String?,
	val parameters: ListElement<ParameterElement>
) : Element {

	companion object {

		@JvmStatic
		fun function(iterator: Grammar.TokenIterator): FunctionElement {
			iterator.expect(KeywordToken.FUNCTION)
			val name = iterator.identifier()

			iterator.expect(SeparatorToken.OPEN_ROUND_BRACKET)
			val parameters = mutableListOf<ParameterElement>()

			while (!iterator.equalTo(SeparatorToken.CLOSE_ROUND_BRACKET)) {
				parameters += ParameterElement.parameter(iterator)
				iterator.skipIf(SeparatorToken.COMMA)
			}
			iterator.skip()

			val type = iterator.returnType()
			iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
			iterator.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

			return FunctionElement(name, type, ListElement.unmodifiable(parameters))
		}
	}
}