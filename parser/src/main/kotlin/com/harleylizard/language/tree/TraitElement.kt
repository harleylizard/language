package com.harleylizard.language.tree

import com.harleylizard.language.asmify.*
import com.harleylizard.language.token.KeywordToken
import com.harleylizard.language.token.SeparatorToken

data class TraitElement(
	override val name: String,
	val type: String?
) : Element, Acceptor, Named {

	override fun accepts(asmify: Asmify) {
		asmify.accept(TraitAsmify(asmify).asmify(this))
	}
}

object TraitParser {

	@JvmStatic
	fun trait(iterator: Grammar.TokenIterator): TraitElement {
		iterator.expect(KeywordToken.TRAIT)
		val name = iterator.identifier()

		var type: String? = null
		if (iterator.skipIf(SeparatorToken.COLON)) {
			type = iterator.identifier()
		}
		iterator.expect(SeparatorToken.OPEN_CURLY_BRACKET)
		iterator.expect(SeparatorToken.CLOSE_CURLY_BRACKET)

		return TraitElement(name, type)
	}
}