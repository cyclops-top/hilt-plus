package top.cyclops.spear.module2.data.remote

import hilt.plus.annotation.HiltApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import top.cyclops.spear.module2.data.model.Singer

@Suppress("unused")
@HiltApi
internal interface SingerApi {
    @GET("/singer/{id}")
    suspend fun getById(@Path("id") id: Long): Singer?

    @GET("/singer/search")
    suspend fun searchByName(@Query("name") name: String): List<Singer>
}