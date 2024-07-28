package com.harleylizard.language.tree

class ParameterTree(val name: String, val type: String, private val reference: Boolean) : Tree {

	fun asmify(imports: Map<String, String>): String {
		val asmType = Asmify.getAsmType(type, imports)
		return asmType.takeUnless { reference } ?: Asmify.getReferenceType(asmType)
	}
}