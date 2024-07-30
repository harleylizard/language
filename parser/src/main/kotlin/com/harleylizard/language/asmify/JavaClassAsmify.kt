package com.harleylizard.language.asmify

import com.harleylizard.language.tree.JavaClassElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class JavaClassAsmify(private val asmify: Asmify) {

	fun asmify(klass: JavaClassElement): ClassNode {
		val interfaces = mutableListOf<String>()
		for (supa in klass.supers) {
			val meta = asmify.meta(supa.name)
			if (meta.jnterface) {
				interfaces += meta.path
			}
		}
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, asmify.name(klass.name), null, "java/lang/Object", interfaces.toTypedArray())

		for (function in klass.functions) {
			FunctionAsmify(asmify).asmify(function).accept(node)
		}
		node.visitEnd()
		return node
	}
}