package com.harleylizard.language.tree

import com.harleylizard.language.parser.Header
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class SubClassTree(override val name: String, private val body: ListTree) : ClassTree {

	override fun asmify(header: Header): ClassNode {
		val className = header.getClassName(name)

		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, className, null, "java/lang/Object", null)
		for (tree in body) {
			if (tree is FunctionTree) {
				tree.asmify(header.imports).accept(node)
			}
		}
		node.visitEnd()
		return node
	}
}