package com.harleylizard.language.old

import java.util.*

@JvmInline
value class ListTree<T : Tree> private constructor(private val list: List<T>) : Tree, Iterable<T> {

	override fun iterator() = list.iterator()

	companion object {

		@JvmStatic
		fun <T : Tree> unmodifiable(list: List<T>) = ListTree<T>(Collections.unmodifiableList(list))

		@JvmStatic
		fun <T : Tree> empty() = ListTree<T>(emptyList())

		@JvmStatic
		fun <T : Tree> singular(t: T) = ListTree(listOf(t))
	}
}