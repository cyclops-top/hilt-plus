package hilt.plus.compiler.room

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import hilt.plus.annotation.HiltRoom
import hilt.plus.compiler.core.AnnotationArguments
import hilt.plus.compiler.core.AnnotationDataFactory

data class HiltRoomData(
    val name: String,
    val version: Int,
    val nodes: List<KSType>,
    val exportSchema: Boolean,
    val autoMigrations: List<KSAnnotation>,
    val inMemory: Boolean,
    val interceptor: KSType,
    val printSql: Boolean
){
    companion object: AnnotationDataFactory<HiltRoomData>{
        override val annotation: ClassName = HiltRoom::class.asClassName()
        override fun create(args: AnnotationArguments): HiltRoomData {
            return HiltRoomData(
                args.get("name"),
                args.get("version"),
                args.get("nodes"),
                args.get("exportSchema"),
                args.get("autoMigrations"),
                args.get("inMemory"),
                args.get("interceptor"),
                args.get("printSql"),
            )
        }
    }
}