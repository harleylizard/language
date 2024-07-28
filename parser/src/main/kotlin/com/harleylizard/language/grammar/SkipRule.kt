package com.harleylizard.language.grammar

class SkipRule : Rule {

	override fun eval(context: Context) {
		context.skip()
	}
}