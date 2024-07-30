package com.harleylizard.language.asmify

import com.harleylizard.language.asmify.Asmify.Companion.getLoadType
import com.harleylizard.language.asmify.Asmify.Companion.getReturnType
import com.harleylizard.language.tree.DataElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class DataAsmify(private val asmify: Asmify) {

	fun asmify(data: DataElement): ClassNode {
		val qualifier = asmify.qualifier(data.name)
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, qualifier, null, "java/lang/Object", null)

		val fields = data.fields
		for (field in fields) {
			val member = node.visitField(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL, field.name, asmify.descriptor(field.type), null, null)
			member.visitEnd()
		}

		val builder = StringBuilder()
		builder.append("(")
		for (field in fields) {
			builder.append(asmify.descriptor(field.type))
		}
		builder.append(")V")
		val constructor = node.visitMethod(Opcodes.ACC_PUBLIC, "<init>", builder.toString(), null, null)
		constructor.visitCode()
		constructor.visitVarInsn(Opcodes.ALOAD, 0)
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		for ((i, field) in fields.withIndex()) {
			val name = field.name
			constructor.visitParameter(name, Opcodes.ACC_PUBLIC)
			constructor.visitVarInsn(Opcodes.ALOAD, 0)

			val type = asmify.descriptor(field.type)
			constructor.visitVarInsn(getLoadType(type), i + 1)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, qualifier, name, type)
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()

		for (operator in data.operators) {
			FunctionAsmify(asmify).asmify(operator).accept(node)
		}

		for (field in fields) {
			val name = field.name
			val type = asmify.descriptor(field.type)
			val getter = node.visitMethod(Opcodes.ACC_PUBLIC, "get$name", "()$type", null, null)
			getter.visitCode()
			getter.visitVarInsn(Opcodes.ALOAD, 0)
			getter.visitFieldInsn(Opcodes.GETFIELD, qualifier, name, type)
			getter.visitInsn(getReturnType(type))
			getter.visitEnd()
		}
		node.visitEnd()
		return node
	}
}