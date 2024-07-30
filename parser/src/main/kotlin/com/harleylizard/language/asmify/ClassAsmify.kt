package com.harleylizard.language.asmify

import com.harleylizard.language.tree.ClassElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ClassAsmify(private val asmify: Asmify) {

	fun asmify(customClass: ClassElement): ClassNode {
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, asmify.name(customClass.name), null, "java/lang/Object", null)

		for (function in customClass.functions) {
			FunctionAsmify(asmify).asmify(function).accept(node)
		}
		node.visitEnd()
		return node
	}
}