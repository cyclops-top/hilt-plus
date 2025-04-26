package top.cyclops.knit

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName


data class SuperClassSpec(val typeName: TypeName, val parameter: CodeBlock? = null)

fun superClass(typeName: ClassName, parameter: (CodeBlockKnit.() -> Unit)? = null): SuperClassSpec {
    return if (parameter == null) {
        SuperClassSpec(typeName)
    } else {
        SuperClassSpec(typeName, codeBlock(parameter))
    }
}