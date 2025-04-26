package top.cyclops.spear.sample.data.local

import androidx.room.Dao
import androidx.room.Query
import hilt.plus.annotation.HiltDao
import top.cyclops.spear.module1.data.model.User

/**
 * 此处仅用于测试hilt-plus 生成没有指定node的测试，正式项目中应该避免该设计
 */
@HiltDao(entities = [User::class])
@Dao
internal interface TestDao {
    @Query("select * from user where id = :id")
    suspend fun findById(id: Long): User
}