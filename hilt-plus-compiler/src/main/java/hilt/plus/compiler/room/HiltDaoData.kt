package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import hilt.plus.annotation.HiltDao
import hilt.plus.compiler.core.AnnotationData

@AnnotationData(HiltDao::class)
data class HiltDaoData(
    val entities: List<KSType>,
    val views: List<KSType>,
    val node: KSType
)
