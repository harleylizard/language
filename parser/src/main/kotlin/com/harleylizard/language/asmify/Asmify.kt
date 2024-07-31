package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*
import kotlin.reflect.KClass

class Asmify private constructor(private val cw: ClassWriter, private val table: Table<Element>) {

	fun accept(node: ClassNode) {
		node.accept(cw)
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

	fun interfaces(supers: ListElement<SuperElement>): List<Reference<InterfaceElement>> {
		val interfaces = mutableListOf<Reference<InterfaceElement>>()
		for (supar in supers) {
			val reference = reference(supar.name, InterfaceElement::class)
			if (reference != null) {
				interfaces += reference
			}
		}
		return Collections.unmodifiableList(interfaces)
	}

	fun traits(supers: ListElement<SuperElement>): List<Reference<TraitElement>> {
		val traits = mutableListOf<Reference<TraitElement>>()
		for (supar in supers) {
			val reference = reference(supar.name, TraitElement::class)
			if (reference != null) {
				traits += reference
			}
		}
		return Collections.unmodifiableList(traits)
	}

	fun qualifier(name: String) = table.qualifier(name)

	private fun <T : Element> reference(name: String, klass: KClass<T>): Reference<T>? = table.reference(name, klass)

	companion object {

		@JvmStatic
		fun immutableAsmify(cw: ClassWriter, syntaxTree: SyntaxTree): Asmify {
			return Asmify(cw, Tables.immutableTable(syntaxTree))
		}

		@JvmStatic
		fun loadType(descriptor: String): Int {
			return when (descriptor) {
				"Z", "C", "B", "S", "I", -> Opcodes.ILOAD
				"J" -> Opcodes.LLOAD
				"F" -> Opcodes.FLOAD
				"D" -> Opcodes.DLOAD
				else -> Opcodes.ALOAD
			}
		}

		@JvmStatic
		fun returnType(descriptor: String): Int {
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