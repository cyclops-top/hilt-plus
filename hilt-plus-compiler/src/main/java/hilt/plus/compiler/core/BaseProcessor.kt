@file:Suppress("unused")

package hilt.plus.compiler.core

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {


    fun option(name: String, default: String): ReadOnlyProperty<BaseProcessor, String> {
        return Option(name, default)
    }

    fun <T> T.log(): T {
        environment.logger.warn("---------> $this")
        return this
    }

   protected fun FileSpec.save(dependencies: List<KSFile>?) {
        writeTo(
            environment.codeGenerator,
            dependencies?.let {
                Dependencies(false, *it.toTypedArray())
            } ?: Dependencies.ALL_FILES
        )
    }

    fun GeneratedFile.save() {
        file.save(dependencies)
    }

    inline fun <reified T : Any> KSAnnotated.findAnnotationData(): T? {
        val parser = HiltAnnotationParser.getParser(T::class)
        return parser.findAnnotation(this)
    }

    inline fun <reified T : Any> Resolver.getSymbolsWithAnnotation(kind: ClassKind): Sequence<AnnotationElement<T>> {
        val parser = HiltAnnotationParser.getParser(T::class)
        return getSymbolsWithAnnotation(parser.annotationType)
            .filterIsType {
                it.classKind == kind
            }
            .mapNotNull { type ->
                parser.findAnnotation(type)?.let {
                    AnnotationElement(type, it)
                }
            }
    }

    private class Option(val name: String, val default: String) :
        ReadOnlyProperty<BaseProcessor, String> {
        private lateinit var value: String
        override fun getValue(thisRef: BaseProcessor, property: KProperty<*>): String {
            if (!this::value.isInitialized) {
                value = thisRef.environment.options.getOrDefault(name, default)
            }
            return value
        }
    }
}