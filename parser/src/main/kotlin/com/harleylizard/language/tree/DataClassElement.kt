package com.harleylizard.language.tree

data class DataClassElement(
	val name: String,
	val fields: ListElement<VariableElement>,
	val operators: ListElement<FunctionElement>
) : ClassElement