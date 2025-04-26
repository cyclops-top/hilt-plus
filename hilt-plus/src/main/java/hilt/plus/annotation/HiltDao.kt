package hilt.plus.annotation

import dagger.hilt.GeneratesRootInput
import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@GeneratesRootInput
annotation class HiltDao(
    val entities: Array<KClass<*>> = [],
    val views: Array<KClass<*>> = [],
    val node: KClass<*> = Any::class
)
