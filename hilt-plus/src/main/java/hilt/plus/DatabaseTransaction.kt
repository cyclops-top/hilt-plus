package hilt.plus

/**
 * 用于提供对数据库的事务
 * @see [hilt.plus.annotation.HiltRoom]
 */
interface DatabaseTransaction {
    /**
     * 在事务中执行相关数据库操作
     * @param block 数据库操作
     */
    suspend fun <R> withTransaction(block: suspend () -> R): R
}