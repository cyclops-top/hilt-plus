package hilt.plus.compiler.core

import com.squareup.kotlinpoet.ClassName

interface AnnotationDataFactory<T> {
    val annotation: ClassName
    fun create(args: AnnotationArguments): T
}