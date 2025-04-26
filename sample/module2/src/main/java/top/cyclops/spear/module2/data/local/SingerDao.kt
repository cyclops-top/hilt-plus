package top.cyclops.spear.module2.data.local

import androidx.room.Dao
import hilt.plus.annotation.HiltDao
import top.cyclops.spear.module2.data.RoomNodeModule2
import top.cyclops.spear.module2.data.model.Singer

@HiltDao(
    entities = [Singer::class], node = RoomNodeModule2::class
)
@Dao
interface SingerDao {}