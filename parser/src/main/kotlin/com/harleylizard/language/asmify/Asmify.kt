package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*

data class Meta(
	val path: String,
	val jnterface: Boolean,
	val subPaths: List<String>
)

class Asmify private constructor(
	private val table: Map<String, Meta>
) {

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
			else -> "L${name(l)};"
		}
		return if (isArray) "[$j" else j
	}

	fun name(name: String) = table[name]?.path ?: name

	fun meta(name: String) = table[name]!!

	companion object {

		@JvmStatic
		fun create(syntaxTree: SyntaxTree): Asmify {
			val map = mutableMapOf<String, Meta>()

			for (clazz in syntaxTree.classes) {
				val path = "${syntaxTree.packageName.replace(".", "/")}/${clazz.name}"
				val jnterface = clazz is InterfaceElement

				map[clazz.name] = Meta(path, jnterface, emptyList())
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