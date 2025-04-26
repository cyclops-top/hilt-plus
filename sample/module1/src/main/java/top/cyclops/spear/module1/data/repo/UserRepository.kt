package top.cyclops.spear.module1.data.repo


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import top.cyclops.spear.module1.data.local.UserDao
import top.cyclops.spear.module1.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun findUserById(id: Long): User?
    fun user(id: Long): Flow<User?>
    suspend fun insert(user: User): User
    suspend fun update(user: User)
}


@Singleton
internal class DefaultUserRepository @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun findUserById(id: Long) = userDao.findById(id)
    override fun user(id: Long): Flow<User?> {
        return userDao.findByIdWithFlow(id)
    }

    override suspend fun insert(user: User): User {
        return userDao.insert(user).let {
            user.copy(id = it)
        }
    }

    override suspend fun update(user: User) {
        userDao.update(user)
    }
}


@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserRepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bean(bean: DefaultUserRepository): UserRepository
}