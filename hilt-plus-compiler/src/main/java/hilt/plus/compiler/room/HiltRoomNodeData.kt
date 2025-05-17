package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltRoomNode
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory

class HiltRoomNodeData(data: AnnotationArguments) {
    val node: KSType by data.property()
    val entities: List<KSType> by data.property()
    val views: List<KSType> by data.property()
    val daoList: List<KSType> by data.property()
    companion object : AnnotationDataFactory<HiltRoomNodeData> {
        override val annotation: ClassName = HiltRoomNode::class.asClassName()

        override fun create(args: AnnotationArguments): HiltRoomNodeData {
            return HiltRoomNodeData(args)
        }
    }
}