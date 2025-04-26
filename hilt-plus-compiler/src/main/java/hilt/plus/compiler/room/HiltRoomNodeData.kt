package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import hilt.plus.annotation.HiltRoomNode
import hilt.plus.compiler.core.AnnotationData

@AnnotationData(HiltRoomNode::class)
data class HiltRoomNodeData(
    val node: KSType,
    val entities: List<KSType>,
    val views: List<KSType>,
    val daoList: List<KSType>,
)