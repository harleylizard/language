package com.harleylizard.language.parser

import com.harleylizard.language.token.*

class Header(override val tokens: Iterator<Token>) : Parser {
	val imports = mutableMapOf<String, String>()

	private val packageName: String

	override var token = tokens.next()

	init {
		if (token != KeywordToken.PACKAGE) {
			throw RuntimeException("missing package")
		}
		skip()
		packageName = identifier().replace(".", "/")
		skip()

		while (token != EOFToken) {
			when (token) {
				KeywordToken.IMPORT -> parseImport().also { imports[it.first] = it.second }
				KeywordToken.CLASS -> parseClass().also { imports[it.first] = it.second }
				KeywordToken.DATA -> parseData().also { imports[it.first] = it.second }
				else -> skip()
			}
		}
	}

	fun getClassName(name: String) = imports[name]

	private fun parseImport(): Pair<String, String> {
		next(KeywordToken.IMPORT)
		val name = identifier()
		skip()
		return Pair(name.substring(name.lastIndexOf(".") + 1, name.length), name.replace(".", "/"))
	}

	private fun parseClass(): Pair<String, String> {
		next(KeywordToken.CLASS)
		val name = identifier()
		skip()
		return createClassName(name)
	}

	private fun parseData(): Pair<String, String> {
		next(KeywordToken.DATA)
		val name = identifier()
		skip()
		return createClassName(name)
	}

	private fun createClassName(name: String) = Pair(name, "$packageName/$name")
}