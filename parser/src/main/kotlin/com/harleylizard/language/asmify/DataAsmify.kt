package com.harleylizard.language.asmify

import com.harleylizard.language.tree.DataElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class DataAsmify(private val asmify: Asmify) {

	fun asmify(data: DataElement): ClassNode {
		val qualifier = asmify.qualifier(data.name)
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, qualifier, null, "java/lang/Object", null)

		val fields = data.fields
		for (field in fields) {
			val access = if (field.setter) Opcodes.ACC_PRIVATE else Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL
			val member = node.visitField(access, field.name, asmify.descriptor(field.type), null, null)
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
			constructor.visitVarInsn(Asmify.loadType(type), i + 1)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, qualifier, name, type)
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()

		for (operator in data.operators) {
			val op = node.visitMethod(Opcodes.ACC_PUBLIC, operator.name, builder.toString(), null, null)
			op.visitCode()
			op.visitEnd()
		}

		for (field in fields) {
			val name = field.name
			val type = asmify.descriptor(field.type)
			val getter = node.visitMethod(Opcodes.ACC_PUBLIC, "get$name", "()$type", null, null)
			getter.visitCode()
			getter.visitVarInsn(Opcodes.ALOAD, 0)
			getter.visitFieldInsn(Opcodes.GETFIELD, qualifier, name, type)
			getter.visitInsn(Asmify.returnType(type))
			getter.visitEnd()

			if (field.setter) {
				val setter = node.visitMethod(Opcodes.ACC_PUBLIC, "set$name", "($type)V", null, null)
				setter.visitCode()
				setter.visitParameter(name, Opcodes.ACC_PUBLIC)
				setter.visitVarInsn(Opcodes.ALOAD, 0)
				setter.visitVarInsn(Asmify.loadType(type), 1)
				setter.visitFieldInsn(Opcodes.PUTFIELD, qualifier, name, type)
				setter.visitInsn(Opcodes.RETURN)
				setter.visitEnd()
			}
		}
		node.visitEnd()
		return node
	}
}