package com.harleylizard.language.tree

data class InterfaceElement(
	override val name: String,
	val functions: ListElement<InterfaceFunctionElement>
) : ClassElement