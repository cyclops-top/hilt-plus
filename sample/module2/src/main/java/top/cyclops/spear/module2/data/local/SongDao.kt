package top.cyclops.spear.module2.data.local

import androidx.room.Dao
import androidx.room.Query
import hilt.plus.annotation.HiltDao
import top.cyclops.spear.module2.data.RoomNodeModule2
import top.cyclops.spear.module2.data.model.Song

@HiltDao(
    entities = [Song::class],
    node = RoomNodeModule2::class
)
@Dao
interface SongDao {
    @Query("select * from song where id = :id")
    suspend fun findById(id: Long): Song
}