package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltDao
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory

class HiltDaoData(data: AnnotationArguments) {
    val entities: List<KSType> by data.property()
    val views: List<KSType> by data.property()
    val node: KSType by data.property()
    companion object : AnnotationDataFactory<HiltDaoData> {
        override val annotation: ClassName = HiltDao::class.asClassName()
        override fun create(args: AnnotationArguments): HiltDaoData {
            return HiltDaoData(args)
        }
    }
}


