package com.harleylizard.language.tree

import com.harleylizard.language.asmify.*
import com.harleylizard.language.token.KeywordToken
import com.harleylizard.language.token.SeparatorToken

data class InterfaceElement(
	val name: String,
	val functions: ListElement<AbstractFunctionElement>
) : Element, Acceptor {

	override fun accepts(asmify: Asmify) {
		asmify.accept(InterfaceAsmify(asmify).asmify(this))
	}
}

data class AbstractFunctionElement(
	val name: String,
	val type: String?,
	val parameters: ListElement<ParameterElement>
) : Element

object InterfaceParser {

	@JvmStatic
	fun jnterface(iterator: Grammar.TokenIterator): InterfaceElement {
		iterator.expect(KeywordToken.INTERFACE)
		val name = iterator.identifier()

		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		val functions = mutableListOf<AbstractFunctionElement>()

		while (!iterator.equalTo(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (iterator.token) {
				KeywordToken.FUNCTION -> functions += function(iterator)
				else -> iterator.skip()
			}
		}
		iterator.skip()
		return InterfaceElement(name, ListElement.unmodifiable(functions))
	}

	@JvmStatic
	fun function(iterator: Grammar.TokenIterator): AbstractFunctionElement {
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
		return AbstractFunctionElement(name, type, ListElement.unmodifiable(parameters))
	}
}