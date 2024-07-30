package com.harleylizard.language.tree

data class InterfaceFunctionElement(
	val name: String,
	val type: String?,
	val parameters: ListElement<VariableElement>
) : Element