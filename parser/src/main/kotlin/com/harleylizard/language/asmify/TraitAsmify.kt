package com.harleylizard.language.asmify

import com.harleylizard.language.tree.TraitElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class TraitAsmify(private val asmify: Asmify) {

	fun asmify(trait: TraitElement): ClassNode {
		val type = trait.type

		val node = ClassNode()
		val qualifier = asmify.qualifier(trait.name)
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, qualifier, null, "java/lang/Object", null)

		if (type != null) {
			val field = node.visitField(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL, type.lowercase(), asmify.descriptor(type), null, null)
			field.visitEnd()
		}

		val descriptor = if (type != null) "(${asmify.descriptor(type)})V" else "()V"
		val constructor = node.visitMethod(Opcodes.ACC_PRIVATE, "<init>", descriptor, null, null)
		constructor.visitCode()
		constructor.visitVarInsn(Opcodes.ALOAD, 0)
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		if (type != null) {
			constructor.visitVarInsn(Opcodes.ALOAD, 0)
			constructor.visitVarInsn(Opcodes.ALOAD, 1)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, qualifier, type.lowercase(), asmify.descriptor(type))
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()

		val returnType = asmify.descriptor(qualifier)
		val staticDescriptor = if (type != null) "(${asmify.descriptor(type)})$returnType" else "()$returnType"
		val instantiation = node.visitMethod(Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC or Opcodes.ACC_SYNTHETIC, "new", staticDescriptor, null, null)
		instantiation.visitCode()
		instantiation.visitTypeInsn(Opcodes.NEW, qualifier)
		instantiation.visitInsn(Opcodes.DUP)
		if (type != null) {
			instantiation.visitVarInsn(Opcodes.ALOAD, 0)
		}
		instantiation.visitMethodInsn(Opcodes.INVOKESPECIAL, qualifier, "<init>", descriptor, false)
		instantiation.visitInsn(Opcodes.ARETURN)
		instantiation.visitEnd()

		node.visitEnd()
		return node
	}
}