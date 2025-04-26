package hilt.plus

interface DatabaseTransaction {
    suspend fun <R> withTransaction(block: suspend () -> R): R
}