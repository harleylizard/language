package com.harleylizard.language.tree

data class ClassElement(
	override val name: String,
	val functions: ListElement<FunctionElement>
) : IClassElement