package com.harleylizard.language.asmify

import com.harleylizard.language.tree.JavaClassElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class JavaClassAsmify(private val asmify: Asmify) {

	fun asmify(klass: JavaClassElement): ClassNode {
		val supers = klass.supers

		val interfaces = mutableListOf<String>()
		for (jnterface in asmify.interfaces(supers)) {
			interfaces += jnterface.qualifier
		}

		val qualifier = asmify.qualifier(klass.name)
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, qualifier, null, "java/lang/Object", interfaces.toTypedArray())

		val traits = asmify.traits(supers)
		for (trait in traits) {
			val name = trait.name
			val field = node.visitField(Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, name.lowercase(), asmify.descriptor(name), null, null)
			field.visitEnd()
		}

		val constructor = node.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
		constructor.visitCode()
		constructor.visitVarInsn(Opcodes.ALOAD, 0)
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)

		for (trait in traits) {
			val name = trait.name
			val returnType = asmify.descriptor(name)
			val type = trait.element.type
			val descriptor = if (type != null) "(${asmify.descriptor(name)})$returnType" else "()$returnType"
			if (type != null) {
				constructor.visitVarInsn(Opcodes.ALOAD, 0)
			}
			constructor.visitVarInsn(Opcodes.ALOAD, 0)
			constructor.visitMethodInsn(Opcodes.INVOKESTATIC, trait.qualifier, "new", descriptor, false)
			constructor.visitFieldInsn(Opcodes.PUTFIELD, qualifier, name.lowercase(), returnType)
		}
		constructor.visitInsn(Opcodes.RETURN)
		constructor.visitEnd()

		for (function in klass.functions) {
			FunctionAsmify(asmify).asmify(function).accept(node)
		}
		node.visitEnd()
		return node
	}

	private fun fieldName(name: String) = name.let { it.substring(it.lastIndexOf("/") + 1).lowercase() }
}