package com.harleylizard.language.old

import org.objectweb.asm.tree.ClassNode

interface ClassTree : Tree {
	val name: String

	fun asmify(): ClassNode
}