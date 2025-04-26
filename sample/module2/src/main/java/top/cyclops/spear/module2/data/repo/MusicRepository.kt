@file:Suppress("unused")

package top.cyclops.spear.module2.data.repo


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import top.cyclops.spear.module2.data.local.SingerDao
import top.cyclops.spear.module2.data.local.SongDao
import top.cyclops.spear.module2.data.remote.SingerApi
import javax.inject.Inject
import javax.inject.Singleton

interface MusicRepository {
    //do something with api or dao
}


@Singleton
internal class DefaultMusicRepository @Inject constructor(
    private val singerApi: SingerApi,
    private val songDao: SongDao,
    private val singerDao: SingerDao,
) : MusicRepository {

}


@Module
@InstallIn(SingletonComponent::class)
abstract class MusicRepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bean(bean: DefaultMusicRepository): MusicRepository
}