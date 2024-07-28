package com.harleylizard.language.grammar

fun interface Rule {

	fun eval(context: Context)

	infix fun then(rule: Rule): Rule {
		return Rule { context ->
			this.eval(context)
			rule.eval(context)
		}
	}
}