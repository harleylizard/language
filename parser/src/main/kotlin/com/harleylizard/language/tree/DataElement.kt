package com.harleylizard.language.tree

import com.harleylizard.language.asmify.*
import com.harleylizard.language.token.*

data class DataElement(
	override val name: String,
	val fields: ListElement<DataFieldElement>,
	val operators: ListElement<OperatorFunctionElement>
) : Element, Acceptor, Named {

	override fun accepts(asmify: Asmify) {
		asmify.accept(DataAsmify(asmify).asmify(this))
	}
}

data class DataFieldElement(
	val name: String,
	val type: String,
	val setter: Boolean
) : Element

data class OperatorFunctionElement(
	val name: String,
	val type: String?,
	val parameter: ParameterElement,
) : Element

object DataParser {

	@JvmStatic
	fun data(iterator: Grammar.TokenIterator): DataElement {
		iterator.expect(KeywordToken.DATA)
		val name = iterator.identifier()

		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		val fields = mutableListOf<DataFieldElement>()
		val operators = mutableListOf<OperatorFunctionElement>()

		while (!iterator.equalTo(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (iterator.token) {
				is IdentifierToken -> fields += field(iterator)
				KeywordToken.OPERATOR -> operators += operator(iterator)
				else -> iterator.skip()
			}
		}
		iterator.skip()
		return DataElement(name, ListElement.unmodifiable(fields), ListElement.unmodifiable(operators))
	}

	@JvmStatic
	fun field(iterator: Grammar.TokenIterator): DataFieldElement {
		val name = iterator.identifier()
		iterator.expect(SeparatorToken.COLON)
		val type = iterator.type()
		var setter = false
		if (iterator.skipIf(SeparatorToken.SEMI_COLON)) {
			setter = iterator.skipIf(KeywordToken.SET)
		}
		return DataFieldElement(name, type, setter)
	}

	@JvmStatic
	fun operator(iterator: Grammar.TokenIterator): OperatorFunctionElement {
		iterator.expect(KeywordToken.OPERATOR)
		iterator.either(
			OperatorToken.ADD,
			OperatorToken.MINUS,
			OperatorToken.MULTIPLY,
			OperatorToken.DIVIDE
		)
		val name = (iterator.token as OperatorToken).asString
		iterator.skip()

		iterator.expect(SeparatorToken.OPEN_ROUND_BRACKET)
		val parameter = ParameterElement.parameter(iterator)
		iterator.expect(SeparatorToken.CLOSE_ROUND_BRACKET)

		val type = iterator.returnType()

		return OperatorFunctionElement(name, type, parameter)
	}
}