package hilt.plus.annotation

import dagger.hilt.GeneratesRootInput
import kotlin.reflect.KClass

/**
 * 用于声明此Dao为可提供注入的
 * ```kotlin
 * @HiltDao(entities = [User::class])
 * @Dao
 * internal interface TestDao {
 *     @Query("select * from user where id = :id")
 *     suspend fun findById(id: Long): User
 * }
 * ```
 * @property entities 该dao下使用到的实体
 * @property views 该dao下使用的视图
 * @property node 该dao注入的节点，可选
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@GeneratesRootInput
annotation class HiltDao(
    val entities: Array<KClass<*>> = [],
    val views: Array<KClass<*>> = [],
    val node: KClass<*> = Any::class
)
