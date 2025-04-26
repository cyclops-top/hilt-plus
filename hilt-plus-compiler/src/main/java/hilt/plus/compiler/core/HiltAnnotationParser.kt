package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Suppress("UNCHECKED_CAST", "unused")
object HiltAnnotationParser {
    private val parserCache = HashMap<KClass<*>, ParserDelegate<*>>()
    inline fun <reified T : Any> parse(annotated: KSAnnotated): T? {
        val parser = getParser(T::class)
        return parser.findAnnotation(annotated)
    }

    inline fun <reified T : Any> parse(annotated: KSAnnotation): T {
        val parser = getParser(T::class)
        return parser.parse(annotated)
    }

    fun <T : Any> getParser(type: KClass<T>): ParserDelegate<T> {
        return parserCache.getOrPut(type) {
            buildDelegate(type)
        } as ParserDelegate<T>
    }

    private fun <T : Any> buildDelegate(type: KClass<T>): ParserDelegate<T> {
        val annotationType = type.findAnnotation<AnnotationData>()?.value
            ?: error("$type not found HiltAnnotation")
        return ParserDelegate(annotationType.asClassName(), type)
    }


    class ParserDelegate<T : Any>(
        val annotationType: ClassName,
        private val annotationData: KClass<T>,
    ) {
        private val creator: (KSAnnotation) -> T by lazy {
            val constructor = annotationData.constructors.first()
            val parameters = constructor.parameters.map { it.name!! }
            return@lazy { annotation ->
                val arguments = Arguments(annotation)
                annotationData.constructors.first()
                    .call(*parameters.map { arguments.get<Any>(it) }.toTypedArray())
            }
        }

        fun parse(annotation: KSAnnotation): T {
            return creator(annotation)
        }

        fun findAnnotation(annotated: KSAnnotated): T? {
            val annotation = annotated.findAnnotation(annotationType) ?: return null
            return creator(annotation)
        }

        fun findAnnotations(annotated: KSAnnotated): List<T> {
            return annotated.findAnnotations(annotationType)
                .map(creator)
        }
    }

    private class Arguments(annotation: KSAnnotation) {
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
}