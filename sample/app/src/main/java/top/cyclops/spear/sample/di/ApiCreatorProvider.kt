package top.cyclops.spear.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hilt.plus.ApiCreator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiCreatorProvider {

    @Singleton
    @Provides
    fun okhttp(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun retrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun apiCreator(retrofit: Retrofit): ApiCreator {
        return object : ApiCreator {
            override fun <T : Any> create(clazz: Class<T>): T {
                return retrofit.create(clazz)
            }
        }
    }
}