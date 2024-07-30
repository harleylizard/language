package com.harleylizard.language.asmify

import com.harleylizard.language.tree.TraitElement
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class TraitAsmify(private val asmify: Asmify) {

	fun asmify(trait: TraitElement): ClassNode {
		val node = ClassNode()
		val access = Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL
		node.visit(Opcodes.V19, access, asmify.name(trait.name), null, "java/lang/Object", null)

		node.visitEnd()
		return node
	}
}