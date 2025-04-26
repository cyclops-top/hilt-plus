package hilt.plus.compiler.core


fun String.lowercaseFirstChar() = replaceFirstChar { it.lowercase() }