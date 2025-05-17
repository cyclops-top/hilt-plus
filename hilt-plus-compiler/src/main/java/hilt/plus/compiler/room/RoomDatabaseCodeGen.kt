package hilt.plus.compiler.room

import androidx.room.Database
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import dagger.Provides
import hilt.plus.DatabaseTransaction
import hilt.plus.annotation.HiltRoom
import hilt.plus.compiler.core.GeneratedFile
import hilt.plus.compiler.core.HiltPlusCodeGen
import hilt.plus.compiler.core.lowercaseFirstChar
import hilt.plus.compiler.core.unionFuncName
import top.cyclops.knit.annotation
import top.cyclops.knit.classType
import top.cyclops.knit.codeBlock
import top.cyclops.knit.codes
import top.cyclops.knit.file
import top.cyclops.knit.func
import top.cyclops.knit.parameter
import top.cyclops.knit.superClass
import top.cyclops.knit.superInterface
import top.cyclops.knit.typeVariable
import javax.inject.Singleton

class RoomDatabaseCodeGen : HiltPlusCodeGen<RoomDatabaseElement, List<GeneratedFile>> {
    private val hiltRoomClass = HiltRoom::class.asClassName()
    override fun generate(source: RoomDatabaseElement): List<GeneratedFile> {
        return listOf(source.genDatabaseFile(), source.genDatabaseModule())
    }


    private fun RoomDatabaseElement.genDatabaseFile(): GeneratedFile {
        if (!isImplementedDatabaseTransaction) {
            error("$type must implement DatabaseTransaction")
        }
        return file(genDatabaseName) {
            import("androidx.room", "withTransaction", "withTransactionExt")
            +classType(genDatabaseName) {
                +abstract
                if (type.isInternal()) {
                    +internal
                }
                +superClass(ClassName("androidx.room", "RoomDatabase"))
                +superInterface(typeName)
                type.annotations.filter {
                    it.annotationType.toTypeName() != hiltRoomClass
                }.forEach {
                    +annotation(it)
                }
                +genRoomAnnotation(entities, views)
                +originatingElementAnnotation(typeName)
                +"withTransaction".func {
                    +override + suspend
                    val rType = +typeVariable("R")
                    +"block".parameter(
                        ClassName("kotlin.coroutines", "SuspendFunction0")
                            .parameterizedBy(rType)
                    )
                    returns(rType)
                    +codeBlock {
                        +codes("return withTransactionExt(block)")
                    }
                }
                daoList.forEach {
                    +it.genDaoFunc()
                }
            }
        }.let {
            GeneratedFile(it)
        }
    }

    private fun RoomDatabaseElement.genRoomAnnotation(
        entities: List<Any>,
        views: List<Any>
    ): AnnotationSpec {
        return annotation(Database::class) {
            "entities" set entities
            "views" set views
            "version" set data.version
            "exportSchema" set data.exportSchema
            "autoMigrations" set data.autoMigrations
        }
    }

    private fun KSClassDeclaration.genDaoFunc(): FunSpec {
        val name = toClassName()
        return name.unionFuncName() func {
            +abstract
            if(isInternal()){
                +internal
            }
            returns(name)
        }
    }


    private fun RoomDatabaseElement.genDatabaseModule(): GeneratedFile {
        return createModuleFile(genModuleName) {
            +originatingElementAnnotation(genDatabaseName)
            if(type.isInternal()){
                +internal
            }
            +genCreateDatabaseFunc()
            +databaseProviderFunc()
            if (hasRoot) {
                +transactionProviderFunc(null)
            }
            providers.forEach {
                +transactionProviderFunc(it)
            }
            daoList.forEach {
                +daoProviderFunc(it)
            }
        }.let {
            GeneratedFile(it)
        }
    }

    private fun RoomDatabaseElement.genCreateDatabaseFunc(): FunSpec {
        val name = typeName.simpleName.lowercaseFirstChar()
        val roomClass = ClassName("androidx.room", "Room")
        val executorsClass = ClassName("java.util.concurrent", "Executors")
        val logClass = ClassName("android.util", "Log")
        return name func {
            +"context".parameter(ClassName("android.content", "Context")) {
                +annotation(ClassName("dagger.hilt.android.qualifiers", "ApplicationContext"))
            }
            +suppressAnnotation("UNCHECKED_CAST")
            +annotation(Provides::class)
            +annotation(Singleton::class)
            +codeBlock {
                +"return "
                if (data.inMemory) {
                    +codes(
                        "%T.inMemoryDatabaseBuilder(context,%T::class.java)\n",
                        roomClass,
                        genDatabaseName
                    )
                } else {
                    +codes(
                        "%T.databaseBuilder(context,%T::class.java,%S)\n",
                        roomClass,
                        genDatabaseName,
                        "${data.name}.db"
                    )
                }
                if (interceptor != null) {
                    +interceptorFunc()
                }
                if (data.printSql) {
                    +codes(
                        """|.setQueryCallback({ sqlQuery, bindArgs ->
                           |   %T.d("SQL","Query: ${'$'}sqlQuery \nSQL Args: ${'$'}bindArgs")
                           |}, %T.newSingleThreadExecutor())""".trimMargin(),
                        logClass,
                        executorsClass
                    )
                }
                +codes(".build()")

            }
            returns(genDatabaseName)
        }
    }

    private fun RoomDatabaseElement.interceptorFunc(): CodeBlock {
        val interceptorType = interceptor!!
        return codeBlock {
            +codes(".let{ builder -> \n\t")
            if (interceptorType.classKind == ClassKind.OBJECT) {
                +codes(
                    "%T.interceptor(builder) ",
                    interceptorType.toClassName(),
                )
            } else if (interceptorType.primaryConstructor?.parameters.isNullOrEmpty()) {
                +codes(
                    "%T().interceptor(builder) ",
                    interceptorType.toClassName(),
                )
            } else {
                +codes(
                    "%T(context).interceptor(builder) ",
                    interceptorType.toClassName(),
                )
            }
            +codes(
                "as %T.Builder<%T>\n", ClassName("androidx.room.", "RoomDatabase"),
                genDatabaseName
            )
            +codes("}\n")
        }
    }


    private fun RoomDatabaseElement.transactionProviderFunc(qualifier: KSClassDeclaration?): FunSpec {
        val className = qualifier?.toClassName()
        val name = className?.unionFuncName() ?: "tx"
        return name func {
            +annotation(Provides::class)
            +annotation(Singleton::class)
            if (className != null) {
                +annotation(className)
            }
            returns(DatabaseTransaction::class.asClassName())

            +"db".parameter(genDatabaseName)
            +codeBlock {
                +"return db"
            }
        }
    }

    private fun RoomDatabaseElement.databaseProviderFunc(): FunSpec {
        val name = typeName.unionFuncName()
        return name func {
            +annotation(Provides::class)
            +annotation(Singleton::class)
            returns(typeName)
            +"db".parameter(genDatabaseName)
            +codeBlock {
                +"return db"
            }
        }
    }

    private fun RoomDatabaseElement.daoProviderFunc(dao: KSClassDeclaration): FunSpec {
        val className = dao.toClassName()
        val name = className.unionFuncName()
        return name func {
            +annotation(Provides::class)
            +annotation(Singleton::class)
            if(dao.isInternal()){
                +internal
            }
            returns(className)

            +"db".parameter(genDatabaseName)
            +codeBlock {
                +codes("return db.%L()", name)
            }
        }
    }


}