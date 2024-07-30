package com.harleylizard.language.tree

data class TraitElement(
	override val name: String,
	val supers: ListElement<SuperElement>
) : ClassElement {}