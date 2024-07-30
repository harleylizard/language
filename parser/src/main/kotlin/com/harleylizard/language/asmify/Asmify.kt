package com.harleylizard.language.asmify

import com.harleylizard.language.tree.*
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*

class Asmify private constructor(private val table: Map<String, String>) {

	fun asmify(clazz: IClassElement): ClassNode {
		if (clazz is DataClassElement) {
			return DataClassAsmify(this).asmify(clazz)
		}
		if (clazz is ClassElement) {
			return ClassAsmify(this).asmify(clazz)
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

	fun name(name: String) = table[name] ?: name

	companion object {

		@JvmStatic
		fun create(syntaxTree: SyntaxTree): Asmify {
			val map = mutableMapOf<String, String>()

			for (clazz in syntaxTree.classes) {
				map[clazz.name] = "${syntaxTree.packageName.replace(".", "/")}/${clazz.name}"
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