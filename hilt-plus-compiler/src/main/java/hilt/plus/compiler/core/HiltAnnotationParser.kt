package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation

class HiltAnnotationParser<T : Any>(private val factory: AnnotationDataFactory<T>) {


    val annotation = factory.annotation
    fun parse(annotation: KSAnnotation): T {
        return factory.create(AnnotationArguments(annotation))
    }

    fun findAnnotation(annotated: KSAnnotated): T? {
        val annotation = annotated.findAnnotation(factory.annotation) ?: return null
        return parse(annotation)
    }

    companion object {
        fun <T : Any> from(factory: AnnotationDataFactory<T>): HiltAnnotationParser<T> {
            return HiltAnnotationParser(factory)
        }
    }

}