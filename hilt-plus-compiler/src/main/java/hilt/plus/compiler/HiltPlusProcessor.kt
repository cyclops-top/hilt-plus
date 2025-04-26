@file:Suppress("unused")

package hilt.plus.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import hilt.plus.compiler.api.ApiProcessor
import hilt.plus.compiler.core.BaseProcessor
import hilt.plus.compiler.room.RoomProcessor

class HiltPlusProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {
    private val subs: List<BaseProcessor> =
        listOf(ApiProcessor(environment), RoomProcessor(environment))

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return subs.map { it.process(resolver) }.flatten()
    }

    @AutoService(SymbolProcessorProvider::class)
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return HiltPlusProcessor(environment)
        }
    }
}