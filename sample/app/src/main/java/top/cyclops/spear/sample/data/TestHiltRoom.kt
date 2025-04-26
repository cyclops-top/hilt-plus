package top.cyclops.spear.sample.data

import androidx.room.TypeConverters
import hilt.plus.DatabaseTransaction
import hilt.plus.annotation.HiltRoom
import top.cyclops.spear.module1.RoomNodeModule1
import top.cyclops.spear.module2.data.Converter
import top.cyclops.spear.module2.data.RoomNodeModule2

@TypeConverters(Converter::class)
@HiltRoom(
    name = "test",
    version = 1,
    nodes = [RoomNodeModule1::class, RoomNodeModule2::class],
    printSql = true,
    interceptor = RoomBuilderInterceptor::class
)
interface TestHiltRoom : DatabaseTransaction {
}