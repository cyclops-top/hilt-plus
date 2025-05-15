package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSAnnotation

class AnnotationArguments(annotation: KSAnnotation) {
    private val arguments = buildMap {
        annotation.defaultArguments.forEach {
            put(it.name!!.asString(), it.value)
        }
        annotation.arguments.forEach {
            put(it.name!!.asString(), it.value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): T {
        return arguments[name] as T
    }

    override fun toString(): String {
        return arguments.toString()
    }
}