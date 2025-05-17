package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSAnnotation
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

    fun <T> property(): ReadOnlyProperty<Any, T> {
        return AnnotationArgumentDelegate()
    }

    private inner class AnnotationArgumentDelegate<T> :
        ReadOnlyProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return get(property.name)
        }
    }
}