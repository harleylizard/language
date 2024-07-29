package com.harleylizard.language.tree

import java.util.*

@JvmInline
value class ListElement<T : Element> private constructor(private val list: List<T>) : Element, Iterable<T> {

	override fun iterator() = list.iterator()

	companion object {

		@JvmStatic
		fun <T : Element> unmodifiable(list: List<T>) = ListElement(Collections.unmodifiableList(list))

		@JvmStatic
		fun <T : Element> singular(t: T) = unmodifiable(listOf(t))
	}
}