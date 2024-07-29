package com.harleylizard.language.token

data object EOFToken : Token {
	override val asString = "eof"

}