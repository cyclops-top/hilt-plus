package hilt.plus

/**
 * room 数据库构建拦截器，用于在构建数据库时进行拦截操作，一般用于初始化数据等操作
 * ```kotlin
 *
 * class RoomBuilderInterceptor(context: Context) : HiltPlusInterceptor<RoomDatabase.Builder<*>> {
 *     override fun interceptor(source: RoomDatabase.Builder<*>): RoomDatabase.Builder<*> {
 *         //do something
 *         return source
 *     }
 * }
 * ```
 * 注：构建函数可以为空，也可有一个context的参数，该context为application的context
 * @see [hilt.plus.annotation.HiltRoom.interceptor]
 *
 */
interface HiltPlusInterceptor<T> {
    fun interceptor(source: T): T
}