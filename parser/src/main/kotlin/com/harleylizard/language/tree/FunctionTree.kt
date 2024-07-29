package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

class FunctionTree(
	val access: Int,
	val name: String,
	val parameters: ListTree<MemberTree>,
	val type: String,
	val body: ListTree<Tree>) : Tree {

	fun asmify(): MethodNode {
		val params = "${createParams()}$type"
		val node = MethodNode(access, name, params, null, null)
		for (parameter in parameters) {
			node.visitParameter(parameter.name, Opcodes.ACC_PUBLIC)
		}
		node.visitEnd()
		return node
	}

	private fun createParams(): String {
		val builder = StringBuilder()
		builder.append("(")
		for (parameter in parameters) {
			builder.append(parameter.type)
		}
		builder.append(")")
		return builder.toString()
	}
}