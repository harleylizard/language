package com.harleylizard.language.token

enum class TypeToken(private val syntaxName: String) : Token {
	BYTE("byte"),
	SHORT("short"),
	INT("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double")
	;

	override val asString: String get() = syntaxName

}