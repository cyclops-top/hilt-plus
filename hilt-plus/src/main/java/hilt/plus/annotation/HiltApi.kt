package hilt.plus.annotation

import dagger.hilt.GeneratesRootInput

/**
 * 声明该api将提供注入实体
 * ```kotlin
 * @HiltApi
 * internal interface SingerApi {
 *     @GET("/singer/{id}")
 *     suspend fun getById(@Path("id") id: Long): Singer?
 *
 *     @GET("/singer/search")
 *     suspend fun searchByName(@Query("name") name: String): List<Singer>
 * }
 * ```
 * 注：使用该注解需要提供可注入的[hilt.plus.ApiCreator]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@GeneratesRootInput
annotation class HiltApi
