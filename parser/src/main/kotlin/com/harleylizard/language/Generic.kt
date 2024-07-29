package com.harleylizard.language

class Generic(val type: String?) {
	val isNumber; get() = type.equals("number")

}