package hilt.plus.compiler.room

import hilt.plus.compiler.core.AnnotationElement

data class RoomNodeElement(
    val node: RoomNodeType,
    val items: List<AnnotationElement<HiltDaoData>>
)

