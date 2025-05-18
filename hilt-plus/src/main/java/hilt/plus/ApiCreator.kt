package hilt.plus

/**
 * 用于创建Api代理，一般使用于Retrofit的构建，当使用[hilt.plus.annotation.HiltApi]时，
 * 必须实现并提供注入方式
 * ```kotlin
 *  @Singleton
 *  @Provides
 *  fun apiCreator(retrofit: Retrofit): ApiCreator {
 *     return object : ApiCreator {
 *        override fun <T : Any> create(clazz: Class<T>): T {
 *           return retrofit.create(clazz)
 *        }
 *     }
 *  }
 * ```
 */
interface ApiCreator {
    /**
     * 创建api的代理
     * @param clazz api的class
     */
    fun <T : Any> create(clazz: Class<T>): T
}