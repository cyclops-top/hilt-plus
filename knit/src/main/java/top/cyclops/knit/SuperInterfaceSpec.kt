package top.cyclops.knit

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

data class SuperInterfaceSpec(val typeName: TypeName, val delegate: CodeBlock? = null)

fun superInterface(
    typeName: TypeName,
    delegate: (CodeBlockKnit.() -> Unit)? = null
): SuperInterfaceSpec {
    return if (delegate == null) {
        SuperInterfaceSpec(typeName)
    } else {
        SuperInterfaceSpec(typeName, codeBlock(delegate))
    }
}