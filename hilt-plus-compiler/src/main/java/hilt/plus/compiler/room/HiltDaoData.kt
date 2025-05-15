package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltDao
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory

data class HiltDaoData(
    val entities: List<KSType>,
    val views: List<KSType>,
    val node: KSType,
) {
    companion object : AnnotationDataFactory<HiltDaoData> {
        override val annotation: ClassName = HiltDao::class.asClassName()

        override fun create(args: AnnotationArguments): HiltDaoData {
            return HiltDaoData(
                args.get("entities"),
                args.get("views"),
                args.get("node"),
            )
        }
    }
}


