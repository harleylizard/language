package com.harleylizard.language

class Template(val type: String?) {
	val isNumber; get() = type.equals("number")

}