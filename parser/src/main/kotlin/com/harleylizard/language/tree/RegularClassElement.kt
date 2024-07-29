package com.harleylizard.language.tree

data class RegularClassElement(
	val name: String,
	val functions: ListElement<FunctionElement>
) : ClassElement