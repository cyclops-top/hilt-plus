package top.cyclops.spear.module2.data

import androidx.room.TypeConverter
import java.util.Date

object Converter {

    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromDate(value: Date): Long {
        return value.time
    }
}