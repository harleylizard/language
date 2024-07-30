package com.harleylizard.language.asmify

import com.harleylizard.language.tree.InterfaceElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class InterfaceAsmify(private val asmify: Asmify) {

	fun asmify(jnterface: InterfaceElement): ClassNode {
		val node = ClassNode()
		val access = Opcodes.ACC_PUBLIC or Opcodes.ACC_INTERFACE or Opcodes.ACC_ABSTRACT
		node.visit(Opcodes.V19, access, asmify.qualifier(jnterface.name), null, "java/lang/Object", null)


		node.visitEnd()
		return node
	}
}