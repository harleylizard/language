package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ObjectClassTree(override val name: String, private val body: ListTree<Tree>) : ClassTree {

	override fun asmify(): ClassNode {
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, name, null, "java/lang/Object", null)
		for (tree in body) {
			if (tree is FunctionTree) {
				tree.asmify().accept(node)
			}
		}
		node.visitEnd()
		return node
	}
}