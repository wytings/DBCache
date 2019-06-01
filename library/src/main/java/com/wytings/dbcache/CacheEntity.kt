package com.wytings.dbcache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Rex.Wei on 2019/6/1.
 *
 * @author 韦玉庭
 */
@Entity(tableName = "t_cache_table")
class CacheEntity(
    @field:PrimaryKey
    @field:ColumnInfo(name = "key")
    val key: String
) {

    @ColumnInfo(name = "value")
    var value: String = ""

    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis()

    @ColumnInfo(name = "update_time")
    var updateTime: Long = 0


    override fun toString(): String {
        return "CacheEntity{" +
                "key='" + key + '\''.toString() +
                ", value='" + value + '\''.toString() +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}'.toString()
    }
}
