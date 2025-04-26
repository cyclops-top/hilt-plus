package top.cyclops.knit

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import top.cyclops.knit.annotation.KnitMarker
import kotlin.reflect.KClass

@KnitMarker
interface AnnotationKnit {
    operator fun CodeBlock.unaryPlus(): CodeBlock
    infix fun String.set(value: Any)

    companion object {
        operator fun invoke(builder: AnnotationSpec.Builder): AnnotationKnit {
            return object : AnnotationKnit {
                override fun CodeBlock.unaryPlus(): CodeBlock {
                    builder.addMember(this)
                    return this
                }

                override fun String.set(value: Any) {
                    builder.addMember(codeBlock {
                        +"${this@set}="
                        +translationValue(value)
                    })
                }

                private fun translationValue(value: Any): CodeBlock {
                    return when (value) {
                        is List<*> -> {
                            codeBlock {
                                +"["
                                value.filterNotNull().forEach {
                                    +translationValue(it)
                                    +","
                                }
                                +"]"
                            }
                        }

                        is KSType -> {
                            val unwrapped = value.unwrapTypeAlias()
                            val isEnum =
                                (unwrapped.declaration as KSClassDeclaration).classKind == ClassKind.ENUM_ENTRY
                            if (isEnum) {
                                val parent =
                                    unwrapped.declaration.parentDeclaration as KSClassDeclaration
                                val entry = unwrapped.declaration.simpleName.getShortName()
                                codeBlock {
                                    +codes("%T.%L", parent.toClassName(), entry)
                                }
                            } else {
                                codeBlock {
                                    +codes("%T::class", unwrapped.toClassName())
                                }
                            }
                        }

                        is KSName -> codeBlock {
                            +codes(
                                "%T.%L",
                                ClassName.bestGuess(value.getQualifier()),
                                value.getShortName(),
                            )
                        }

                        is KSAnnotation -> codeBlock {
                            +codes("%L", value.toAnnotationSpec(true))
                        }

                        else -> translationScalarsValue(value)
                    }
                }


                private fun KSType.unwrapTypeAlias(): KSType {
                    return if (this.declaration is KSTypeAlias) {
                        (this.declaration as KSTypeAlias).type.resolve()
                    } else {
                        this
                    }
                }

                private fun translationScalarsValue(value: Any): CodeBlock {
                    return codeBlock {
                        when (value) {
                            is Class<*> -> +codes("%T::class", value)
                            is ClassName -> +codes("%T::class", value)
                            is Enum<*> -> +codes("%T.%L", value.javaClass, value.name)
                            is String -> +codes("%S", value)
                            is Float -> +codes("%Lf", value)
                            is Double -> +codes("%L", value)
                            is Char -> +codes("'%L'", value)
                            is Byte -> +codes("$value.toByte()")
                            is Short -> +codes("$value.toShort()")
                            // Int or Boolean
                            else -> +codes("%L", value)
                        }
                    }
                }
            }
        }
    }
}

fun annotation(type: ClassName, block: AnnotationKnit.() -> Unit = {}): AnnotationSpec {
    val builder = AnnotationSpec.builder(type)
    AnnotationKnit(builder).block()
    return builder.build()
}

fun annotation(clazz: KClass<*>, block: AnnotationKnit.() -> Unit = {}): AnnotationSpec {
    return annotation(clazz.asClassName(), block)
}


fun annotation(clazz: KSAnnotation): AnnotationSpec {
    val typeName = clazz.annotationType.resolve().toTypeName()
    val builder = if (typeName is ClassName) {
        AnnotationSpec.builder(typeName)
    } else {
        AnnotationSpec.builder(typeName as ParameterizedTypeName)
    }
    with(AnnotationKnit(builder)) {
        clazz.arguments.forEach {
            if (it.name != null && it.value != null) {
                it.name!!.getShortName() set it.value!!
            }
        }
    }
    return builder.build()
}