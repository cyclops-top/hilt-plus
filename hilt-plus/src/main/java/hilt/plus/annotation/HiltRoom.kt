@file:Suppress("unused")

package hilt.plus.annotation

import androidx.room.AutoMigration
import dagger.hilt.GeneratesRootInput
import hilt.plus.HiltPlusInterceptor
import kotlin.reflect.KClass

/**
 * @property nodes see[HiltRoomNode]
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

