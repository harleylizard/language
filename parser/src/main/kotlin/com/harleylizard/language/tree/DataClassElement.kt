package com.harleylizard.language.tree

data class DataClassElement(
	override val name: String,
	val fields: ListElement<VariableElement>,
	val operators: ListElement<FunctionElement>
) : IClassElement