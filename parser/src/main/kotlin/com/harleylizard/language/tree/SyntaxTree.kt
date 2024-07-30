package com.harleylizard.language.tree

data class SyntaxTree(
	val packageName: String,
	val imports: Map<String, String>,
	val generics: Map<String, String>,
	val classes: ListElement<ClassElement>
) : Element