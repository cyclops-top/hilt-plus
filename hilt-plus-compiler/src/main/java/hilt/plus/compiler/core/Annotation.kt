@file:Suppress("unused")

package hilt.plus.compiler.core

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

fun Resolver.getSymbolsWithAnnotation(className: ClassName): Sequence<KSAnnotated> {
    return getSymbolsWithAnnotation(className.canonicalName)
}


fun Sequence<KSAnnotated>.filterIsType(predicate: (KSClassDeclaration) -> Boolean): Sequence<KSClassDeclaration> {
    return filterIsInstance<KSClassDeclaration>()
        .filter(predicate)
}

fun Sequence<KSAnnotated>.filterIsInterface(): Sequence<KSClassDeclaration> {
    return filterIsType { it.classKind == ClassKind.INTERFACE }
}

fun Sequence<KSAnnotated>.filterIsClass(): Sequence<KSClassDeclaration> {
    return filterIsType { it.classKind == ClassKind.CLASS }
}


fun Sequence<KSAnnotated>.filterIsEnum(): Sequence<KSClassDeclaration> {
    return filterIsType { it.classKind == ClassKind.ENUM_CLASS }
}

fun Sequence<KSAnnotated>.filterIsAnnotation(): Sequence<KSClassDeclaration> {
    return filterIsType { it.classKind == ClassKind.ANNOTATION_CLASS }
}


fun KSAnnotated.findAnnotation(className: ClassName): KSAnnotation? {
    return annotations.find {
        it.annotationType.resolve().toClassName() == className
    }
}

fun KSAnnotated.findAnnotations(className: ClassName): List<KSAnnotation> {
    return annotations.filter {
        it.annotationType.resolve().toClassName() == className
    }.toList()
}