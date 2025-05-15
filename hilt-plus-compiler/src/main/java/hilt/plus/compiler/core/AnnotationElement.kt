package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName

data class AnnotationElement<T>(
    val type: KSClassDeclaration,
    val data: T
) {
    val typeName = type.toClassName()
}