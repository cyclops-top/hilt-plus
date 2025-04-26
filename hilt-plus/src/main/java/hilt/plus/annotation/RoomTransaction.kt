@file:Suppress("unused")

package hilt.plus.annotation

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoomTransaction(
    val value: String
)
