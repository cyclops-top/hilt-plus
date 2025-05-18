@file:Suppress("unused")

package hilt.plus.annotation

import androidx.room.AutoMigration
import dagger.hilt.GeneratesRootInput
import hilt.plus.HiltPlusInterceptor
import kotlin.reflect.KClass

/**
 * 用于定义数据库
 * ```kotlin
 *
 * @TypeConverters(Converter::class)
 * @HiltRoom(
 *     name = "test",
 *     version = 1,
 *     nodes = [RoomNodeModule1::class, RoomNodeModule2::class],
 *     printSql = true,
 *     interceptor = RoomBuilderInterceptor::class
 * )
 * internal interface TestHiltRoom : DatabaseTransaction
 * ```
 * @property name 数据库名称
 * @property version 数据库版本
 * @property nodes 数据库模块节点，[HiltDao.node] 指向同一自定义的注解
 * @property exportSchema 是否导出schema
 * @property autoMigrations 自动迁移配置
 * @property inMemory 内存数据库
 * @property interceptor 构建拦截器
 * @property printSql 是否打印sql日志
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@GeneratesRootInput
annotation class HiltRoom(
    val name: String,
    val version: Int,
    val nodes: Array<KClass<*>> = [],
    val exportSchema: Boolean = false,
    val autoMigrations: Array<AutoMigration> = [],
    val inMemory: Boolean = false,
    val interceptor: KClass<out HiltPlusInterceptor<*>> = HiltPlusInterceptor::class,
    val printSql: Boolean = false
)

