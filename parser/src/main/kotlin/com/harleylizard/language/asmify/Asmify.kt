package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*
import kotlin.reflect.KClass

class Asmify private constructor(private val table: Table<*>) {

	fun asmify(klass: ClassElement): ClassNode {
		if (klass is DataElement) {
			return DataAsmify(this).asmify(klass)
		}
		if (klass is JavaClassElement) {
			return JavaClassAsmify(this).asmify(klass)
		}
		if (klass is InterfaceElement) {
			return InterfaceAsmify(this).asmify(klass)
		}
		if (klass is TraitElement) {
			return TraitAsmify(this).asmify(klass)
		}
		throw RuntimeException("illegal class")
	}

	fun descriptor(type: String): String {
		var l = type
		val isArray = type.startsWith("[")
		if (isArray) {
			l = type.substring(type.indexOf("[") + 1, type.length)
		}
		val j = when (l) {
			"byte" -> "B"
			"short" -> "S"
			"int" -> "I"
			"long" -> "J"
			"float" -> "F"
			else -> "L${qualifier(l)};"
		}
		return if (isArray) "[$j" else j
	}

	fun qualifier(name: String) = table[name]?.qualifier ?: name

	private fun <T : ClassElement> info(name: String, klass: KClass<T>): ClassInfo<T>? {
		val info = table[name]
		if (info != null && info.element::class == klass) {
			return info as ClassInfo<T>
		}
		return null
	}

	fun interfaces(supers: ListElement<SuperElement>): List<ClassInfo<InterfaceElement>> {
		val interfaces = mutableListOf<ClassInfo<InterfaceElement>>()
		for (supar in supers) {
			val info = info(supar.name, InterfaceElement::class)
			if (info != null) {
				interfaces += info
			}
		}
		return Collections.unmodifiableList(interfaces)
	}

	fun traits(supers: ListElement<SuperElement>): List<ClassInfo<TraitElement>> {
		val traits = mutableListOf<ClassInfo<TraitElement>>()
		for (supar in supers) {
			val info = info(supar.name, TraitElement::class)
			if (info != null) {
				traits += info
			}
		}
		return Collections.unmodifiableList(traits)
	}

	companion object {

		@JvmStatic
		fun create(syntaxTree: SyntaxTree): Asmify {
			val map = mutableMapOf<String, ClassInfo<*>>()

			for (klass in syntaxTree.classes) {
				val name = klass.name
				val qualifier = "${syntaxTree.packageName.replace(".", "/")}/$name"

				map[name] = ClassInfo(name, qualifier, emptyList(), klass)
			}
			return Asmify(Collections.unmodifiableMap(map))
		}

		@JvmStatic
		fun getLoadType(descriptor: String): Int {
			return when (descriptor) {
				"Z", "C", "B", "S", "I", -> Opcodes.ILOAD
				"J" -> Opcodes.LLOAD
				"F" -> Opcodes.FLOAD
				"D" -> Opcodes.DLOAD
				else -> Opcodes.ALOAD
			}
		}

		@JvmStatic
		fun getReturnType(descriptor: String): Int {
			return when (descriptor) {
				"Z", "C", "B", "S", "I", -> Opcodes.IRETURN
				"J" -> Opcodes.LRETURN
				"F" -> Opcodes.FRETURN
				"D" -> Opcodes.DRETURN
				"V" -> Opcodes.RETURN
				else -> Opcodes.ARETURN
			}
		}
	}
}