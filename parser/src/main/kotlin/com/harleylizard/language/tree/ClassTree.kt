package com.harleylizard.language.tree

import com.harleylizard.language.parser.Header
import org.objectweb.asm.tree.ClassNode

interface ClassTree : Tree {
	val name: String

	fun asmify(header: Header): ClassNode
}