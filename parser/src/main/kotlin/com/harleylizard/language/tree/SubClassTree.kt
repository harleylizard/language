package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class SubClassTree(override val name: String, private val body: ListTree) : ClassTree {

	override fun asmify(packageName: String, imports: Map<String, String>): ClassNode {
		val className = className(packageName, name)

		val node = ClassNode()
		node.visit(Opcodes.V19, Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL, className, null, "java/lang/Object", null)
		for (tree in body) {
			if (tree is FunctionTree) {
				tree.asmify(imports).accept(node)
			}
		}
		node.visitEnd()
		return node
	}

	companion object {
		@JvmStatic
		fun className(packageName: String, name: String) = "$packageName/$name"
	}
}