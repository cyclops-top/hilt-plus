package hilt.plus.compiler.core

import com.squareup.kotlinpoet.ClassName


fun ClassName.unionFuncName(): String {
    return canonicalName.replace(".", "_").let { "`$it`" }
}