package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

/** @author justin on 2023/11/29 */
@Target(AnnotationTarget.CLASS)
annotation class AnnotationData(
    val value: KClass<out Annotation>,
)


data class AnnotationElement<T>(
    val type: KSClassDeclaration,
    val data: T
) {
    val typeName = type.toClassName()
}