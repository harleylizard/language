package com.harleylizard.language.asmify

import com.harleylizard.language.asmify.Asmify.Companion.getLoadType
import com.harleylizard.language.asmify.Asmify.Companion.getReturnType
import com.harleylizard.language.tree.DataElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class DataAsmify(private val asmify: Asmify) {

	fun asmify(data: DataElement): ClassNode {
		val name = asmify.name(data.name)
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, name, null, "java/lang/Object", null)

		val fields = data.fields
		for (field in fields) {
			val member = FieldNode(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL, field.name, asmify.descriptor(field.type), null, null)
			member.visitEnd()
			member.accept(node)
		}

		val builder = StringBuilder()
		builder.append("(")
		for (field in fields) {
			builder.append(asmify.descriptor(field.type))
		}
		builder.append(")V")
		val constructor = MethodNode(Opcodes.ACC_PUBLIC, "<init>", builder.toString(), null, null)
		constructor.visitCode()
		constructor.visitVarInsn(Opcodes.ALOAD, 0)
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		for ((i, field) in fields.withIndex()) {
			constructor.visitParameter(field.name, Opcodes.ACC_PUBLIC)
			constructor.visitVarInsn(Opcodes.ALOAD, 0)

			val type = asmify.descriptor(field.type)
			constructor.visitVarInsn(getLoadType(type), i + 1)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, name, field.name, type)
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()
		constructor.accept(node)

		for (operator in data.operators) {
			FunctionAsmify(asmify).asmify(operator).accept(node)
		}

		for (field in fields) {
			val type = asmify.descriptor(field.type)
			val getter = MethodNode(Opcodes.ACC_PUBLIC, "get${field.name}", "()$type", null, null)
			getter.visitCode()
			getter.visitVarInsn(Opcodes.ALOAD, 0)
			getter.visitFieldInsn(Opcodes.GETFIELD, name, field.name, type)
			getter.visitInsn(getReturnType(type))
			getter.visitEnd()
			getter.accept(node)
		}
		node.visitEnd()
		return node
	}
}