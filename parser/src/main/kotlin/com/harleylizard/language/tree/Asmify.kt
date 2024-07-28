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
}