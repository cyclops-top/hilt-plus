@file:Suppress("unused")

package hilt.plus.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltRoomNode(
    val node: KClass<*>,
    val entities: Array<KClass<*>> = [],
    val views: Array<KClass<*>> = [],
    val daoList: Array<KClass<*>> = []
)