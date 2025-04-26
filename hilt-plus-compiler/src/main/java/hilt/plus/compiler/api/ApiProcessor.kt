package hilt.plus.compiler.api

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import hilt.plus.annotation.HiltApi
import hilt.plus.compiler.core.BaseProcessor
import hilt.plus.compiler.core.filterIsAnnotation
import hilt.plus.compiler.core.filterIsInterface
import hilt.plus.compiler.core.getSymbolsWithAnnotation
import javax.inject.Qualifier

class ApiProcessor(environment: SymbolProcessorEnvironment) : BaseProcessor(environment) {
    private val hiltApiClass = HiltApi::class.asClassName()
    private val qualifierClass = Qualifier::class.asClassName()

    private val codeGen = ApiElementCodeGen()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val qualifiers = resolver.getSymbolsWithAnnotation(qualifierClass)
            .filterIsAnnotation()
            .associate { it.toClassName() to it.containingFile }

        val elements = resolver.getSymbolsWithAnnotation(hiltApiClass)
            .filterIsInterface()
            .map { type ->
                val annotation = type.annotations.mapNotNull {
                    val file = qualifiers[it.annotationType.resolve().toClassName()]
                    if (file != null) {
                        it to file
                    } else {
                        null
                    }
                }.toList()
                ApiElement(
                    type,
                    annotation.map { it.first },
                    annotation.map { it.second } + type.containingFile!!
                )
            }
            .toList()
        if (elements.isNotEmpty()) {
            generateCode(elements)
        }
        return emptyList()
    }

    private fun generateCode(elements: List<ApiElement>) {
        elements.map {
            codeGen.generate(it)
        }.forEach {
            it.save()
        }
    }
}