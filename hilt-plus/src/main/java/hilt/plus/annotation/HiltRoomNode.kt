@file:Suppress("unused")

package hilt.plus.annotation

import kotlin.reflect.KClass

/**
 * 由在[HiltDao.node]中指向的同一自定义注解，代码自动生成。
 * @property node 生成来源的节点
 * @property entities 该节点下包含的所有实体
 * @property views 该节点下包含的所有视图
 * @property daoList 该节点下包含的所有Dao接口
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltRoomNode(
    val node: KClass<*>,
    val entities: Array<KClass<*>> = [],
    val views: Array<KClass<*>> = [],
    val daoList: Array<KClass<*>> = []
)