package hilt.plus.compiler.room

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getKotlinClassByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import hilt.plus.compiler.core.AnnotationElement
import hilt.plus.compiler.core.BaseProcessor
import hilt.plus.compiler.core.HiltAnnotationParser

class RoomProcessor(environment: SymbolProcessorEnvironment) : BaseProcessor(environment) {
    private val roomNodeCodeGen = RoomNodeCodeGen()
    private val hiltRoomNodeDataParser by lazy {
        HiltAnnotationParser.from(HiltRoomNodeData)
    }
    private val roomDatabaseCodeGen = RoomDatabaseCodeGen()
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val nodes = resolver.getSymbolsWithAnnotation<HiltDaoData>(HiltDaoData, ClassKind.INTERFACE)
            .groupBy { it.data.node }
            .map { (node, items) ->
                RoomNodeElement(RoomNodeType(node), items)
            }
        nodes.filter { !it.node.isRoot }.forEach {
            roomNodeCodeGen.generate(it).save()
        }
        val rootNode = nodes.find { it.node.isRoot }

        val databaseElements =
            resolver.getSymbolsWithAnnotation<HiltRoomData>(HiltRoomData, ClassKind.INTERFACE)
                .toList()
        runCatching {
            databaseElements.map { data ->
                val ns = data.data.nodes.map {
                    resolver.loadNode(RoomNodeType(it))
                }
                RoomDatabaseElement(data, rootNode, ns)
            }
        }.onFailure {
            val dao = rootNode?.items?.map { element ->
                element.type
            } ?: emptyList()
            return (databaseElements.map { it.type } + (rootNode?.node?.value as? KSAnnotated) + dao).filterNotNull()
        }.onSuccess { elements ->
            elements.forEach {
                roomDatabaseCodeGen.generate(it).forEach { file ->
                    file.save()
                }
            }
        }
        return emptyList()
    }


    @OptIn(KspExperimental::class)
    private fun Resolver.loadNode(node: RoomNodeType): AnnotationElement<HiltRoomNodeData> {
        val nodeType = getKotlinClassByName(node.daoProviderType.canonicalName)
            ?: error("not found ${node.daoProviderType}")
        val data =
            hiltRoomNodeDataParser.findAnnotation(nodeType)
                ?: error("${node.daoProviderType} must add @HiltRoomNode")
        return AnnotationElement(nodeType, data)
    }
}