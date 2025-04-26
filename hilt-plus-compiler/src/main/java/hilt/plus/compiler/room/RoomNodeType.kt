package hilt.plus.compiler.room

import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

data class RoomNodeType(val value: KSType) {
    val className = value.toClassName()
    val daoProviderType = ClassName(className.packageName, className.simpleName + "_Provider")
    val containingFile = value.declaration.containingFile
    val isInternal = value.declaration.isInternal()
    val isRoot = className == Any::class.asClassName()
}