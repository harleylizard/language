package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

class FunctionTree(
	private val access: Int,
	private val name: String,
	private val parameters: List<ParameterTree>,
	private val type: String,
	private val body: ListTree) : Tree {

	fun asmify(imports: Map<String, String>): MethodNode {
		val descriptor = "${createParameters(imports)}$type"
		val node = MethodNode(access, name, descriptor, null, null)
		for (parameter in parameters) {
			node.visitParameter(parameter.name, Opcodes.ACC_PUBLIC)
		}
		node.visitEnd()
		return node
	}

	private fun createParameters(imports: Map<String, String>): String {
		val builder = StringBuilder()
		builder.append("(")
		for (parameter in parameters) {
			builder.append(parameter.asmify(imports))
		}
		builder.append(")")
		return builder.toString()
	}
}