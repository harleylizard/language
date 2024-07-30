package com.harleylizard.language.tree

data class JavaClassElement(
	override val name: String,
	val supers: ListElement<SuperElement>,
	val functions: ListElement<FunctionElement>
) : ClassElement