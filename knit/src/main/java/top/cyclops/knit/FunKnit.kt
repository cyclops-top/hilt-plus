package top.cyclops.knit

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import top.cyclops.knit.annotation.KnitMarker

@KnitMarker
interface FunKnit : ModifierSupport, FunModifiers {
    operator fun TypeVariableName.unaryPlus(): TypeVariableName
    operator fun ParameterSpec.unaryPlus(): ParameterSpec
    operator fun CodeBlock.unaryPlus(): CodeBlock
    operator fun AnnotationSpec.unaryPlus(): AnnotationSpec
    infix fun returns(type: TypeName)
    fun returns(type: TypeName, code: CodeBlockKnit.() -> Unit)

    companion object {
        operator fun invoke(builder: FunSpec.Builder): FunKnit {
            return object : FunKnit {
                override fun KModifier.unaryPlus(): KModifier = apply {
                    builder.addModifiers(this)
                }

                override fun TypeVariableName.unaryPlus(): TypeVariableName {
                    builder.addTypeVariable(this)
                    return this
                }

                override fun AnnotationSpec.unaryPlus(): AnnotationSpec {
                    builder.addAnnotation(this)
                    return this
                }


                override fun ParameterSpec.unaryPlus(): ParameterSpec {
                    builder.addParameter(this)
                    return this
                }

                override fun CodeBlock.unaryPlus(): CodeBlock {
                    builder.addCode(this)
                    return this
                }

                override fun returns(type: TypeName) {
                    builder.returns(type)
                }

                override fun returns(type: TypeName, code: CodeBlockKnit.() -> Unit) {
                    builder.returns(type, codeBlock(code))
                }
            }
        }
    }
}

infix fun String.func(block: FunKnit.() -> Unit): FunSpec {
    val builder = FunSpec.builder(this)
    FunKnit(builder).block()
    return builder.build()
}
