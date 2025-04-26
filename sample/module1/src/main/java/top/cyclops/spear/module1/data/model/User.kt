package top.cyclops.spear.module1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * [top.cyclops.spear.module2.data.Converter]
 * 理论上Converter不应该放这个module2模块，但是为了演示，所以分开来这样处理，
 * 最好是放一个公共模块，或者是app模块下
 */
@Entity
data class User(
    @PrimaryKey
    val id: Long,
    val name: String,
    val createAt: Date
)
