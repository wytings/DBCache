package com.wytings.dbcache

import android.app.Application
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Rex.Wei on 2019/6/1.
 *
 * @author Rex.Wei
 */
class DBCacheMgr private constructor(zone: String) {

    private val cacheZone = "$zone$SUFFIX"

    private fun cacheDao(): ICacheDao {
        return AbsRoomDatabase.obtain().cacheDao()
    }

    private fun getRealKey(key: String): String {
        return cacheZone + key
    }


    fun save(map: Map<String, Any>?) {
        if (map.isNullOrEmpty()) {
            return
        }
        val entities = mutableListOf<CacheEntity>()
        for ((key, value) in map) {
            val cacheEntity = CacheEntity(getRealKey(key))
            cacheEntity.value = objectToString(value)
            cacheEntity.updateTime = System.currentTimeMillis()
            entities.add(cacheEntity)
        }
        cacheDao().insertOrReplaceEntities(*entities.toTypedArray())
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

    fun save(key: String, obj: Any) {
        try {
            save(mapOf(key to objectToString(obj)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun objectToString(obj: Any): String {
        return when (obj) {
            is Number -> obj.toString()
            is String -> obj
            else -> jsonParser.to(obj)
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
        return cacheDao().queryNumberOfRows("$cacheZone%")
    }

    fun getAll(): Map<String, String> {
        val entities = cacheDao().queryAllEntities("$cacheZone%")
        val map = HashMap<String, String>(entities.size)
        for (entity in entities) {
            map[entity.key.replaceFirst(cacheZone, "")] = entity.value
        }
        return map
    }

    fun deleteAll() {
        cacheDao().deleteAll("$cacheZone%")
    }


    companion object {
        private const val SUFFIX = "$$$"
        private lateinit var jsonParser: IJSONParser

        @JvmStatic
        @JvmOverloads
        fun initialize(application: Application, parser: IJSONParser = DefaultJsonParser()) {
            AbsRoomDatabase.initialize(application)
            jsonParser = parser
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
}
