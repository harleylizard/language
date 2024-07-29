package com.harleylizard.language.tree

@JvmInline
value class ListTree<T : Tree>(private val list: List<T>) : Tree, Iterable<T> {

	override fun iterator() = list.iterator()
}