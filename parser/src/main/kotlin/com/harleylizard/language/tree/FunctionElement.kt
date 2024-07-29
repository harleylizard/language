package com.harleylizard.language.tree

data class FunctionElement(
	val name: String,
	val type: String?,
	val parameters: ListElement<VariableElement>
) : Element