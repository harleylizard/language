package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ClassTree(
	val name: String,
	private val body: ListTree) : Tree {

	fun asmify(): ClassNode {
		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC, name, null, "java/lang/Object", null)
		for (tree in body) {
			if (tree is FunctionTree) {
				tree.asmify().accept(node)
			}
		}
		node.visitEnd()
		return node
	}
}