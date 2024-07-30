package com.harleylizard.language.asmify

import com.harleylizard.language.tree.FunctionElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

class FunctionAsmify(private val asmify: Asmify) {

	fun asmify(function: FunctionElement): MethodNode {
		val builder = StringBuilder()
		builder.append("(")
		for (parameter in function.parameters) {
			builder.append(asmify.descriptor(parameter.type))
		}
		builder.append(")")
		builder.append(function.type?.let(asmify::descriptor) ?: "V")
		val node = MethodNode(Opcodes.ACC_PUBLIC, function.name, builder.toString(), null, null)
		node.visitCode()
		for (parameter in function.parameters) {
			node.visitParameter(parameter.name, Opcodes.ACC_PUBLIC)
		}
		node.visitEnd()
		return node
	}
}