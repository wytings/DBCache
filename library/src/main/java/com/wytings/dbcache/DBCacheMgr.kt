package com.wytings.dbcache

import java.lang.Exception
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Rex.Wei on 2019/6/1.
 *
 * @author 韦玉庭
 */
class DBCacheMgr private constructor(private val zone: String) {

    companion object {
        var jsonParser: IJSONParser = EmptyJsonParser()
            @JvmStatic
            set(value) {
                field = value
            }

        private val managerMap = ConcurrentHashMap<String, DBCacheMgr>()
        @JvmStatic
        fun obtain(zone: String): DBCacheMgr {
            var manager = managerMap[zone]
            if (manager == null) {
                manager = DBCacheMgr(zone)
                managerMap[zone] = manager
            }
            return manager
        }
    }


    private fun cacheDao(): ICacheDao {
        return AbsRoomDatabase.obtain().cacheDao()
    }

    private fun getRealKey(key: String): String {
        return zone + key
    }


    fun save(map: Map<String, String>?) {
        if (map.isNullOrEmpty()) {
            return
        }
        val entities = mutableListOf<CacheEntity>()
        var i = 0
        for ((key, value) in map) {
            val cacheEntity = CacheEntity(getRealKey(key))
            cacheEntity.value = value
            cacheEntity.updateTime = System.currentTimeMillis()
            entities[i++] = cacheEntity
        }
        cacheDao().insertOrReplaceEntities(*entities.toTypedArray())
    }

    fun save(key: String, jsonObject: Any) {
        try {
            save(key, jsonParser.to(jsonObject))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        val entity = cacheDao().querySingleEntity(getRealKey(key)) ?: return defaultValue
        return entity.value
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return getDouble(key, defaultValue.toDouble()).toLong()
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        val value = getString(key, "").toDoubleOrNull()
        return value ?: defaultValue
    }

    fun <T> getObject(key: String, typeOfT: Type): T? {
        val value = getString(key, "")
        return try {
            jsonParser.from(value, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun delete(vararg keys: String) {
        if (keys.isEmpty()) {
            return
        }
        val entities = mutableListOf<CacheEntity>()
        for (i in keys.indices) {
            entities[i] = CacheEntity(getRealKey(keys[i]))
        }
        cacheDao().deleteEntities(*entities.toTypedArray())
    }

    fun getCount(): Long {
        return cacheDao().queryNumberOfRows("$zone%")
    }

    fun getAll(): Map<String, String> {
        val entities = cacheDao().queryAllEntities("$zone%")
        val map = HashMap<String, String>(entities.size)
        for (entity in entities) {
            map[entity.key.replaceFirst(zone.toRegex(), "")] = entity.value
        }
        return map
    }

    fun deleteAll() {
        cacheDao().deleteAll("$zone%")
    }
}
