package com.harleylizard.language.old

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class DataClassTree(
	override val name: String,
	private val fields: ListTree<MemberTree>,
	private val operators: ListTree<FunctionTree>
) : ClassTree {

	override fun asmify(): ClassNode {
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, name, null, "java/lang/Object", null)

		for (field in fields) {
			val type = field.type
			val name = field.name
			val fieldNode = FieldNode(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL, name, type, null, null)
			fieldNode.visitEnd()
			fieldNode.accept(node)

			val methodNode = MethodNode(Opcodes.ACC_PUBLIC, "get$${name}", "()$type", null, null)
			methodNode.visitCode()
			methodNode.visitVarInsn(Opcodes.ALOAD, 0)
			methodNode.visitFieldInsn(Opcodes.GETFIELD, name, name, type)
			methodNode.visitInsn(Asmify.getReturnType(type))
			methodNode.visitEnd()
			methodNode.accept(node)
		}

		val methodNode = MethodNode(Opcodes.ACC_PUBLIC, "<init>", descriptor(), null, null)
		methodNode.visitCode()
		methodNode.visitVarInsn(Opcodes.ALOAD, 0)
		methodNode.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		for ((i, field) in fields.withIndex()) {
			methodNode.visitParameter(field.name, Opcodes.ACC_PUBLIC)
			methodNode.visitVarInsn(Opcodes.ALOAD, 0)

			val type = field.type
			methodNode.visitVarInsn(Asmify.getLoadType(type), i + 1)
			methodNode.visitFieldInsn(Opcodes.PUTFIELD, name, field.name, type)
		}
		methodNode.visitInsn(Opcodes.RETURN)
		methodNode.visitEnd()
		methodNode.accept(node)

		for (function in operators) {
			function.asmify().accept(node)
		}
		node.visitEnd()
		return node
	}

	private fun descriptor(): String {
		val builder = StringBuilder()
		builder.append("(")
		for (field in fields) {
			builder.append(field.type)
		}
		builder.append(")V")
		return builder.toString()
	}
}