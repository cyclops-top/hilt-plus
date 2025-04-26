package hilt.plus.annotation

import dagger.hilt.GeneratesRootInput

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@GeneratesRootInput
annotation class HiltApi
