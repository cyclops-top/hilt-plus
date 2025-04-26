package hilt.plus.compiler.api

import dagger.Provides
import hilt.plus.ApiCreator
import hilt.plus.compiler.core.GeneratedFile
import hilt.plus.compiler.core.HiltPlusCodeGen
import top.cyclops.knit.annotation
import top.cyclops.knit.codeBlock
import top.cyclops.knit.codes
import top.cyclops.knit.func
import top.cyclops.knit.parameter
import javax.inject.Singleton

class ApiElementCodeGen : HiltPlusCodeGen<ApiElement, GeneratedFile> {
    override fun generate(source: ApiElement): GeneratedFile {
        val file = createModuleFile(source.moduleClass, source.typeName) {
            source.target.modifiers.forEach {
                +it
            }
            +source.funName.func {
                +annotation(Provides::class)
                +annotation(Singleton::class)
                +"creator".parameter(ApiCreator::class) {
                    source.retainAnnotations.forEach {
                        +annotation(it)
                    }
                }
                +codeBlock {
                    +codes("return creator.create(%T::class.java)\n", source.typeName)
                }
                returns(source.typeName)
            }
        }
        return GeneratedFile(file, source.dependencies)
    }
}