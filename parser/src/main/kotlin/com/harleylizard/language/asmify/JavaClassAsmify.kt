package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class JavaClassAsmify(private val asmify: Asmify) {

	fun asmify(klass: JavaClassElement): ClassNode {
		val interfaces = mutableListOf<String>()
		val traits = mutableListOf<String>()

		for (supa in klass.supers) {
			val meta = asmify.meta(supa.name)
			when {
				meta.isOf(InterfaceElement::class) -> interfaces += meta.path
				meta.isOf(TraitElement::class) -> traits += meta.path
			}
		}
		val name = asmify.name(klass.name)
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, name, null, "java/lang/Object", interfaces.toTypedArray())

		for (trait in traits) {
			val traitName = trait.substring(trait.lastIndexOf("/") + 1).lowercase()
			val field = FieldNode(Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, traitName, asmify.descriptor(trait), null, null)
			field.visitEnd()
			field.accept(node)
		}
		val constructor = MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
		constructor.visitCode()
		constructor.visitVarInsn(Opcodes.ALOAD, 0)
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
		for (trait in traits) {
			val traitName = trait.substring(trait.lastIndexOf("/") + 1).lowercase()
			val type = asmify.descriptor(trait)
			constructor.visitVarInsn(Opcodes.ALOAD, 0)
			constructor.visitTypeInsn(Opcodes.NEW, type)
			constructor.visitInsn(Opcodes.DUP)
			constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, type, "<init>", "()V", false)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, name, traitName, type)
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()
		constructor.accept(node)

		for (function in klass.functions) {
			FunctionAsmify(asmify).asmify(function).accept(node)
		}
		node.visitEnd()
		return node
	}
}