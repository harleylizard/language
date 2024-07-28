package com.harleylizard.language.tree

import org.objectweb.asm.tree.MethodNode

class FunctionTree(
	private val access: Int,
	private val name: String,
	private val parameters: List<ParameterTree>,
	private val type: String,
	private val body: ListTree) : Tree {
	private val descriptor: String; get() = "()" + "V"

	fun asmify(): MethodNode {
		val node = MethodNode(access, name, descriptor, null, null)

		node.visitEnd()
		return node
	}
}