package hilt.plus.compiler.room

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import hilt.plus.DatabaseTransaction
import hilt.plus.compiler.core.AnnotationElement

private val databaseTransactionClass = DatabaseTransaction::class.asClassName()

data class RoomDatabaseElement(
    val type: KSClassDeclaration,
    val data: HiltRoomData,
    val entities: List<KSType>,
    val views: List<KSType>,
    val daoList: List<KSClassDeclaration>,
    val providers: List<KSClassDeclaration>,
    val hasRoot: Boolean,
) {
    val typeName = type.toClassName()
    val genDatabaseName = ClassName(typeName.packageName, typeName.simpleName + "_Database")
    val genModuleName =
        ClassName(genDatabaseName.packageName, genDatabaseName.simpleName + "_Module")
    val isImplementedDatabaseTransaction = type.getAllSuperTypes().any {
        it.toClassName() == databaseTransactionClass
    }
    val interceptor: KSClassDeclaration? by lazy {
        if ((data.interceptor.declaration as KSClassDeclaration).classKind == ClassKind.INTERFACE) {
            null
        } else {
            data.interceptor.declaration as KSClassDeclaration
        }
    }

    companion object {
        operator fun invoke(
            data: AnnotationElement<HiltRoomData>,
            root: RoomNodeElement?,
            nodes: List<AnnotationElement<HiltRoomNodeData>>
        ): RoomDatabaseElement {
            val rootEntities = root?.items?.map { it.data.entities }?.flatten() ?: emptyList()
            val rootViews = root?.items?.map { it.data.views }?.flatten() ?: emptyList()
            val rootDaoList = root?.items?.map { it.type } ?: emptyList()
            val entities =
                rootEntities + nodes.map { it.data.entities }
                    .flatten()
            val views =
                rootViews + nodes.map { it.data.views }
                    .flatten()
            val daoList =
                rootDaoList + nodes.map { node -> node.data.daoList.map { it.declaration as KSClassDeclaration } }
                    .flatten()
            return RoomDatabaseElement(
                type = data.type,
                data = data.data,
                entities = entities.distinct(),
                views = views.distinct(),
                daoList = daoList.distinct(),
                providers = nodes.map { it.data.node.declaration as KSClassDeclaration },
                hasRoot = root != null
            )
        }

    }
}