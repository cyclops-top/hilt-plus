package hilt.plus

interface ApiCreator {
    fun <T : Any> create(clazz: Class<T>): T
}