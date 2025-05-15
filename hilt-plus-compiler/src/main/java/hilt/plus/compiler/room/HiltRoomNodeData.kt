package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltRoomNode
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory

data class HiltRoomNodeData(
    val node: KSType,
    val entities: List<KSType>,
    val views: List<KSType>,
    val daoList: List<KSType>,
) {
    companion object : AnnotationDataFactory<HiltRoomNodeData> {
        override val annotation: ClassName = HiltRoomNode::class.asClassName()

        override fun create(args: AnnotationArguments): HiltRoomNodeData {
            return HiltRoomNodeData(
                args.get("node"),
                args.get("entities"),
                args.get("views"),
                args.get("daoList"),
            )
        }
    }
}