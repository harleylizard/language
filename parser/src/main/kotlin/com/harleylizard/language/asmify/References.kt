package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import java.util.*
import kotlin.reflect.KClass

data class Reference<T : Element>(
	val name: String,
	val qualifier: String,
	val subPaths: List<String>,
	val element: T
)

typealias Table<T> = Map<String, Reference<T>>

fun <T : Element> Table<T>.qualifier(name: String) = this[name]?.qualifier ?: name

@Suppress("UNCHECKED_CAST")
fun <T : Element, J : T> Table<T>.reference(name: String, klass: KClass<J>): Reference<J>? {
	val reference = this[name]
	if (reference != null && reference.element::class == klass) {
		return reference as Reference<J>
	}
	return null
}

object Tables {

	@JvmStatic
	fun immutableTable(sourceName: String, syntaxTree: SyntaxTree): Table<Element> {
		val packageName = syntaxTree.packageName.replace(".", "/")
		val map = mutableMapOf<String, Reference<Element>>()

		for (element in syntaxTree.elements) {
			if (element is Named) {
				val name = element.name
				val qualifier = "$packageName/$name"

				map[name] = Reference(name, qualifier, emptyList(), element)
			}
		}
		map[sourceName] = Reference(sourceName, "$packageName/$sourceName", emptyList(), EmptyElement)
		return Collections.unmodifiableMap(map)
	}
}