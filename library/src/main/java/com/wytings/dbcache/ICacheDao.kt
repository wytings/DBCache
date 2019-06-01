package com.wytings.dbcache

import androidx.room.*

/**
 * Created by Rex.Wei on 2019/6/1.
 *
 * @author 韦玉庭
 */
@Dao
interface ICacheDao {

    @Query("SELECT * FROM t_cache_table  WHERE `key` = :key LIMIT 1")
    fun querySingleEntity(key: String): CacheEntity?

    @Query("SELECT * FROM t_cache_table WHERE `key` IN (:keys) ORDER BY update_time DESC")
    fun queryEntities(vararg keys: String): List<CacheEntity>

    @Query("SELECT * FROM t_cache_table WHERE `key` LIKE :like ORDER BY update_time DESC")
    fun queryAllEntities(like: String): List<CacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceEntities(vararg entities: CacheEntity): LongArray

    @Query("SELECT COUNT(*) FROM t_cache_table WHERE `key` LIKE :like")
    fun queryNumberOfRows(like: String): Long

    @Query("DELETE FROM t_cache_table WHERE `key` LIKE :like")
    fun deleteAll(like: String)

    @Delete
    fun deleteEntities(vararg entities: CacheEntity): Int
}
