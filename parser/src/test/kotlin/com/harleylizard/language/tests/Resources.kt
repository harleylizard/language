package com.harleylizard.language.tests

import java.io.*

object Resources {

	@JvmStatic
	fun openStream(path: String): InputStream {
		return Resources::class.java.classLoader.getResource(path)?.openStream() ?: throw NullPointerException("")
	}

	@JvmStatic
	fun readString(path: String): String {
		BufferedReader(InputStreamReader(openStream(path))).use { reader ->
			val builder = StringBuilder()
			var line: String?
			while (reader.readLine().also { line = it } != null) {
				builder.append(line).append("\n")
			}

			return builder.toString()
		}
	}
}