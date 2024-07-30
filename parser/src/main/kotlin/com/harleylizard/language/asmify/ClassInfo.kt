package com.harleylizard.language.asmify

import com.harleylizard.language.tree.ClassElement

data class ClassInfo<T : ClassElement>(
	val name: String,
	val qualifier: String,
	val subPaths: List<String>,
	val element: T
)

typealias Table<T> = Map<String, ClassInfo<T>>