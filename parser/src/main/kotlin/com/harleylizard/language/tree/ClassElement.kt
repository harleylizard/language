package com.harleylizard.language.tree

import com.harleylizard.language.asmify.*
import com.harleylizard.language.token.*

data class ClassElement(
	override val name: String,
	val supers: ListElement<SuperElement>,
	val functions: ListElement<FunctionElement>
) : Element, Acceptor, Named {

	override fun accepts(asmify: Asmify) {
		asmify.accept(ClassAsmify(asmify).asmify(this))
	}
}

object ClassParser {

	@JvmStatic
	fun klass(iterator: Grammar.TokenIterator): ClassElement {
		iterator.expect(KeywordToken.CLASS)
		val name = iterator.identifier()

		val supers = supers(iterator)
		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		val functions = mutableListOf<FunctionElement>()

		while (!iterator.equalTo(SeparatorToken.CLOSE_CURLY_BRACKET)) {
			when (iterator.token) {
				KeywordToken.FUNCTION -> functions += FunctionElement.function(iterator)
				else -> iterator.skip()
			}
		}
		iterator.skip()
		return ClassElement(name, supers, ListElement.unmodifiable(functions))
	}

	@JvmStatic
	fun supers(iterator: Grammar.TokenIterator): ListElement<SuperElement> {
		val supers = mutableListOf<SuperElement>()
		if (iterator.skipIf(SeparatorToken.COLON)) {
			while (!iterator.equalTo(SeparatorToken.OPEN_CURLY_BRACKET)) {
				supers += SuperElement(iterator.identifier())

				if (iterator.token is IdentifierToken) {
					iterator.skip()
				}
				if (iterator.equalTo(SeparatorToken.COMMA)) {
					iterator.skip()
				}
			}
		}
		return ListElement.unmodifiable(supers)
	}
}