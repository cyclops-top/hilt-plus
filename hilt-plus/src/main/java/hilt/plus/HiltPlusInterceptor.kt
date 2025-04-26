package hilt.plus

interface HiltPlusInterceptor<T> {
    fun interceptor(source: T): T
}