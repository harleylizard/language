package com.harleylizard.language.asmify

import com.harleylizard.language.tree.ClassElement
import kotlin.reflect.KClass

data class ClassMeta<T : ClassElement>(
	val path: String,
	val klass: KClass<T>,
	val subPaths: List<String>
) {
	fun isOf(klass: KClass<*>) = this.klass == klass
}

typealias Table = Map<String, ClassMeta<*>>