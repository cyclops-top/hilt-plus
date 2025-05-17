package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltRoom
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory


class HiltRoomData(data: AnnotationArguments) {
    val name: String by data.property()
    val version: Int by data.property()
    val nodes: List<KSType> by data.property()
    val exportSchema: Boolean by data.property()
    val autoMigrations: List<KSAnnotation> by data.property()
    val inMemory: Boolean by data.property()
    val interceptor: KSType by data.property()
    val printSql: Boolean by data.property()

    companion object : AnnotationDataFactory<HiltRoomData> {
        override val annotation: ClassName = HiltRoom::class.asClassName()
        override fun create(args: AnnotationArguments): HiltRoomData {
            return HiltRoomData(args)
        }
    }
}
