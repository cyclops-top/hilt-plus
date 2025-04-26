package top.cyclops.knit

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import top.cyclops.knit.annotation.KnitMarker

@KnitMarker
interface TypeKnit : ModifierSupport {

    operator fun SuperInterfaceSpec.unaryPlus(): SuperInterfaceSpec
    operator fun TypeVariableName.unaryPlus(): TypeVariableName
    operator fun AnnotationSpec.unaryPlus(): AnnotationSpec
    operator fun PropertySpec.unaryPlus(): PropertySpec
    operator fun FunSpec.unaryPlus(): FunSpec
    operator fun CodeBlock.unaryPlus(): CodeBlock

    companion object {
        operator fun invoke(builder: TypeSpec.Builder): TypeKnit {
            return object : TypeKnit {
                override fun KModifier.unaryPlus(): KModifier = apply {
                    builder.addModifiers(this)
                }

                override fun SuperInterfaceSpec.unaryPlus(): SuperInterfaceSpec = apply {
                    if (delegate == null) {
                        builder.addSuperinterface(typeName)
                    } else {
                        builder.addSuperinterface(typeName, delegate)
                    }
                }

                override fun AnnotationSpec.unaryPlus(): AnnotationSpec {
                    builder.addAnnotation(this)
                    return this
                }

                override fun TypeVariableName.unaryPlus(): TypeVariableName {
                    builder.addTypeVariable(this)
                    return this
                }

                override fun PropertySpec.unaryPlus(): PropertySpec {
                    builder.addProperty(this)
                    return this
                }

                override fun CodeBlock.unaryPlus(): CodeBlock {
                    builder.addInitializerBlock(this)
                    return this
                }

                override fun FunSpec.unaryPlus(): FunSpec {
                    builder.addFunction(this)
                    return this
                }
            }
        }
    }
}

interface ClassTypeKnit : TypeKnit, ClassModifiers {
    operator fun ConstructorSpec.unaryPlus(): ConstructorSpec
    operator fun SuperClassSpec.unaryPlus(): SuperClassSpec

    companion object {
        operator fun invoke(builder: TypeSpec.Builder): ClassTypeKnit {

            return object : ClassTypeKnit, TypeKnit by TypeKnit(builder) {
                override fun SuperClassSpec.unaryPlus(): SuperClassSpec = apply {
                    builder.superclass(typeName)
                    if (parameter != null) {
                        builder.addSuperclassConstructorParameter(parameter)
                    }
                }

                override fun ConstructorSpec.unaryPlus() = apply {
                    builder.primaryConstructor(this.value)
                }
            }
        }
    }
}


interface ObjectTypeKnit : TypeKnit, ClassModifiers {
    operator fun SuperClassSpec.unaryPlus(): SuperClassSpec

    companion object {
        operator fun invoke(builder: TypeSpec.Builder): ObjectTypeKnit {

            return object : ObjectTypeKnit, TypeKnit by TypeKnit(builder) {
                override fun SuperClassSpec.unaryPlus(): SuperClassSpec = apply {
                    builder.superclass(typeName)
                    if (parameter != null) {
                        builder.addSuperclassConstructorParameter(parameter)
                    }
                }
            }
        }
    }
}

interface InterfaceTypeKnit : TypeKnit, InterfaceModifiers {
    companion object {
        operator fun invoke(builder: TypeSpec.Builder): InterfaceTypeKnit {

            return object : InterfaceTypeKnit, TypeKnit by TypeKnit(builder) {}
        }
    }
}

fun classType(className: ClassName, block: ClassTypeKnit.() -> Unit): TypeSpec {
    val builder = TypeSpec.classBuilder(className)
    ClassTypeKnit(builder).block()
    return builder.build()
}


fun interfaceType(className: ClassName, block: InterfaceTypeKnit.() -> Unit): TypeSpec {
    val builder = TypeSpec.interfaceBuilder(className)
    InterfaceTypeKnit(builder).block()
    return builder.build()
}


fun objectType(className: ClassName, block: ObjectTypeKnit.() -> Unit): TypeSpec {
    val builder = TypeSpec.objectBuilder(className)
    ObjectTypeKnit(builder).block()
    return builder.build()
}