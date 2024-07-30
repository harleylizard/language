package com.harleylizard.language.tree

data class DataElement(
	override val name: String,
	val fields: ListElement<VariableElement>,
	val operators: ListElement<FunctionElement>
) : ClassElement