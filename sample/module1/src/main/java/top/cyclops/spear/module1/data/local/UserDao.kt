package top.cyclops.spear.module1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hilt.plus.annotation.HiltDao
import kotlinx.coroutines.flow.Flow
import top.cyclops.spear.module1.RoomNodeModule1
import top.cyclops.spear.module1.data.model.User

@HiltDao(
    entities = [User::class],
    node = RoomNodeModule1::class
)
@Dao
interface UserDao {
    @Query("select * from user where id = :id")
    suspend fun findById(id: Long): User?

    @Query("select * from user where id = :id")
    fun findByIdWithFlow(id: Long): Flow<User?>

    @Insert(entity = User::class)
    suspend fun insert(user: User): Long

    @Update(entity = User::class)
    suspend fun update(user: User)
}