package com.harleylizard.language.tree

import com.harleylizard.language.parser.Header
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class DataTree(override val name: String, private val fields: List<ParameterTree>) : ClassTree {

	override fun asmify(header: Header): ClassNode {
		val className = header.getClassName(name)

		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, className, null, "java/lang/Object", null)

		for (field in fields) {
			val asmType = field.asmify(header.imports)
			val name = field.name
			val fieldNode = FieldNode(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL, name, asmType, null, null)
			fieldNode.visitEnd()
			fieldNode.accept(node)

			val methodNode = MethodNode(Opcodes.ACC_PUBLIC, "get$${name}", "()$asmType", null, null)
			methodNode.visitCode()
			methodNode.visitVarInsn(Opcodes.ALOAD, 0)
			methodNode.visitFieldInsn(Opcodes.GETFIELD, className, name, asmType)
			methodNode.visitInsn(Asmify.getReturnType(asmType))
			methodNode.visitEnd()
			methodNode.accept(node)
		}

		val methodNode = MethodNode(Opcodes.ACC_PUBLIC, "<init>", descriptor(header.imports), null, null)
		methodNode.visitCode()
		methodNode.visitVarInsn(Opcodes.ALOAD, 0)
		methodNode.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		for ((i, field) in fields.withIndex()) {
			methodNode.visitParameter(field.name, Opcodes.ACC_PUBLIC)

			methodNode.visitVarInsn(Opcodes.ALOAD, 0)

			val asmType = field.asmify(header.imports)
			methodNode.visitVarInsn(Asmify.getLoadType(asmType), i + 1)
			methodNode.visitFieldInsn(Opcodes.PUTFIELD, className, field.name, asmType)
		}
		methodNode.visitInsn(Opcodes.RETURN)
		methodNode.visitEnd()
		methodNode.accept(node)

		node.visitEnd()
		return node
	}

	private fun descriptor(imports: Map<String, String>): String {
		val builder = StringBuilder()
		builder.append("(")
		for (field in fields) {
			builder.append(field.asmify(imports))
		}
		builder.append(")V")
		return builder.toString()
	}
}