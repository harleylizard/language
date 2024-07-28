package com.harleylizard.language.tree

import org.objectweb.asm.tree.ClassNode

interface ClassTree : Tree {
	val name: String

	fun asmify(packageName: String, imports: Map<String, String>): ClassNode
}