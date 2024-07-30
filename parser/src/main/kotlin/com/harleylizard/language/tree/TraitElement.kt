package com.harleylizard.language.tree

data class TraitElement(
	override val name: String,
	val type: String?
) : ClassElement {}