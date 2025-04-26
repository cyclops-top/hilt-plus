package top.cyclops.knit

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import kotlin.reflect.KClass

interface ParameterKnit : ModifierSupport, ParameterModifiers {
    operator fun AnnotationSpec.unaryPlus(): AnnotationSpec

    companion object {
        operator fun invoke(builder: ParameterSpec.Builder): ParameterKnit {
            return object : ParameterKnit {

                override fun KModifier.unaryPlus(): KModifier = apply {
                    builder.addModifiers(this)
                }

                override fun AnnotationSpec.unaryPlus(): AnnotationSpec {
                    builder.addAnnotation(this)
                    return this
                }
            }
        }
    }
}

fun String.parameter(typeName: TypeName, block: ParameterKnit.() -> Unit = {}): ParameterSpec {
    val builder = ParameterSpec.builder(this, typeName)
    ParameterKnit(builder).block()
    return builder.build()
}

fun String.parameter(clazz: KClass<*>, block: ParameterKnit.() -> Unit = {}): ParameterSpec {
    return parameter(clazz.asClassName(), block)
}