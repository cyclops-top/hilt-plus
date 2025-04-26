package top.cyclops.spear.module2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey
    val id: Long,
    val name: String,
    val singer: Long?
)

