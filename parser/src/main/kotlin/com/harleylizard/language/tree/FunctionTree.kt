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
			val type = parameter.type
			builder.append(get(type, imports))
		}
		builder.append(")")
		return builder.toString()
	}

	companion object {
		@JvmStatic
		fun get(type: String, imports: Map<String, String>): String {
			var reference = type

			val array = type.startsWith("[")
			if (array) {
				reference = reference.substring(1)
			}
			if (imports.containsKey(reference)) {
				return when {
					array -> "[L${imports[reference]};"
					else -> "L${imports[reference]};"
				}
			}
			return type
		}
	}
}