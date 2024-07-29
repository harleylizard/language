package com.harleylizard.language.old

import org.objectweb.asm.Opcodes

class Asmify(
	private val map: Map<String, String>,
	private val generics: Map<String, Generic>) {

	fun asDescriptor(type: String): String {
		var mut = type
		val isArray = type.startsWith("[")
		if (isArray) {
			mut = mut.substring(1)
		}
		if (generics.containsKey(mut)) {
			val generic = generics[mut]!!
			val genericType = generic.type
			if (genericType != null) {
				if (generic.isNumber) {
					return "Ljava/lang/Number;"
				}
				return "L${asClass(genericType)};"
			}
			return "Ljava/lang/Object;"
 		}

		if (map.containsKey(mut)) {
			val asm = "L${map[mut]};"
			return "[$asm".takeIf { isArray } ?: asm
		}
		return type
	}

	fun asClass(type: String): String {
		return map[type].takeIf { map.containsKey(type) } ?: asDescriptor(type)
	}

	fun asReference(descriptor: String): String {
		val l = when (descriptor) {
			"Z" -> "com/harleylizard/language/BooleanReference"
			"C" -> "com/harleylizard/language/CharReference"
			"B" -> "com/harleylizard/language/ByteReference"
			"S" -> "com/harleylizard/language/ShortReference"
			"I" -> "com/harleylizard/language/IntReference"
			"J" -> "com/harleylizard/language/LongReference"
			"F" -> "com/harleylizard/language/FloatReference"
			"D" -> "com/harleylizard/language/DoubleReference"
			else -> "com/harleylizard/language/Reference"
		}
		return "L$l;"
	}

	companion object {
		val numbers = listOf("B", "S", "I", "J", "F", "D")

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
	}
}