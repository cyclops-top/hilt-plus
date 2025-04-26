package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import hilt.plus.annotation.HiltRoom
import hilt.plus.compiler.core.AnnotationData

@AnnotationData(HiltRoom::class)
data class HiltRoomData(
    val name: String,
    val version: Int,
    val nodes: List<KSType>,
    val exportSchema: Boolean,
    val autoMigrations: List<KSAnnotation>,
    val inMemory: Boolean,
    val printSql: Boolean,
)