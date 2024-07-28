package com.harleylizard.language.tree

import org.objectweb.asm.Opcodes

object Asmify {

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

	@JvmStatic
	fun getAsmType(type: String, imports: Map<String, String>): String {
		var s = type

		val array = type.startsWith("[")
		if (array) {
			s = s.substring(1)
		}
		if (imports.containsKey(s)) {
			return when {
				array -> "[L${imports[s]};"
				else -> "L${imports[s]};"
			}
		}
		return type
	}

	fun getReferenceType(descriptor: String): String {
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
}