package hilt.plus.compiler.room

import com.squareup.kotlinpoet.AnnotationSpec
import hilt.plus.annotation.HiltRoomNode
import hilt.plus.compiler.core.GeneratedFile
import hilt.plus.compiler.core.HiltPlusCodeGen
import top.cyclops.knit.annotation
import top.cyclops.knit.file
import top.cyclops.knit.interfaceType

class RoomNodeCodeGen : HiltPlusCodeGen<RoomNodeElement, GeneratedFile> {

    override fun generate(source: RoomNodeElement): GeneratedFile {
        val file = file(source.node.daoProviderType) {
            +interfaceType(source.node.daoProviderType) {
                if (source.node.isInternal) {
                    +internal
                }
                +originatingElementAnnotation(source.node.className)
                +source.genNodeAnnotation()
            }
        }
        return GeneratedFile(file, source.dependencies)
    }

    private fun RoomNodeElement.genNodeAnnotation(): AnnotationSpec {
        val entities = items.map {
            it.data.entities
        }.flatten().distinct()
        val views = items.map { it.data.views }.flatten().distinct()
        val daoList = items.map { it.typeName }
        return annotation(HiltRoomNode::class) {
            "node" set node.className
            "entities" set entities
            "views" set views
            "daoList" set daoList
        }
    }
}