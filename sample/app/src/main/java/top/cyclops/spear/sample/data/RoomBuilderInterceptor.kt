@file:Suppress("UNUSED_PARAMETER")

package top.cyclops.spear.sample.data

import android.content.Context
import androidx.room.RoomDatabase
import hilt.plus.HiltPlusInterceptor

class RoomBuilderInterceptor(context: Context) : HiltPlusInterceptor<RoomDatabase.Builder<*>> {
    override fun interceptor(source: RoomDatabase.Builder<*>): RoomDatabase.Builder<*> {
        //do something
        return source
    }
}