package hilt.plus.compiler.core

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.asClassName
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.codegen.OriginatingElement
import dagger.hilt.components.SingletonComponent
import top.cyclops.knit.ObjectTypeKnit
import top.cyclops.knit.annotation
import top.cyclops.knit.codeBlock
import top.cyclops.knit.codes
import top.cyclops.knit.file
import top.cyclops.knit.objectType
import kotlin.reflect.KClass

interface HiltPlusCodeGen<T, R> {
    fun generate(source: T): R

    fun createModuleFile(
        className: ClassName,
        topLevel: ClassName? = null,
        installIn: ClassName = SingletonComponent::class.asClassName(),
        block: ObjectTypeKnit.() -> Unit
    ): FileSpec {
        return file(className) {
            +objectType(className) {
                +annotation(Module::class.asClassName())
                +annotation(InstallIn::class.asClassName()) {
                    +codeBlock {
                        +codes("%T::class", installIn)
                    }
                }
                if (topLevel != null) {
                    +annotation(OriginatingElement::class) {
                        +codeBlock {
                            +codes("topLevelClass=%T::class", topLevel)
                        }
                    }
                }
                block(this)
            }
        }
    }

    fun originatingElementAnnotation(topLevel: KClass<*>): AnnotationSpec {
        return originatingElementAnnotation(topLevel.asClassName())
    }

    fun originatingElementAnnotation(topLevel: ClassName): AnnotationSpec {
        return annotation(OriginatingElement::class) {
            +codeBlock {
                +codes("topLevelClass=%T::class", topLevel)
            }
        }
    }

    fun suppressAnnotation(vararg names:String):AnnotationSpec{
        return annotation(Suppress::class){
            "names" set names.toList()
        }
    }
}