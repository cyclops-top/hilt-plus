package hilt.plus.compiler.api

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import hilt.plus.compiler.core.lowercaseFirstChar

data class ApiElement(
    val target: KSClassDeclaration,
    val retainAnnotations: List<KSAnnotation>,
    val dependencies: List<KSFile>
) {
    val typeName = target.toClassName()
    val moduleClass = ClassName(typeName.packageName, typeName.simpleName + "_Module")
    val funName = typeName.simpleName.lowercaseFirstChar()

}
