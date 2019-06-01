package com.wytings.dbcache

import android.app.Application
import android.text.TextUtils
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Rex.Wei on 2019/6/1.
 *
 * @author 韦玉庭 weiyuting@bytedance.com
 */

@Database(entities = [CacheEntity::class], version = 1)
abstract class AbsRoomDatabase : RoomDatabase() {


    abstract fun cacheDao(): ICacheDao

    companion object {

        private val dataBaseMap = ConcurrentHashMap<String, AbsRoomDatabase>()

        private const val DB_NAME = "cache_room.db"

        @Volatile
        private var application: Application? = null
        private var defaultDatabasePath = ""
            get() {
                if (TextUtils.isEmpty(field)) {
                    field = File(application!!.filesDir, DB_NAME).absolutePath
                }
                return field
            }

        @JvmStatic
        fun initialize(app: Application) {
            application = app
        }

        @JvmStatic
        @Synchronized
        fun obtain(absoluteDatabasePath: String = defaultDatabasePath): AbsRoomDatabase {
            var manager = dataBaseMap[absoluteDatabasePath]
            if (manager == null) {
                manager = Room.databaseBuilder(application!!, AbsRoomDatabase::class.java, absoluteDatabasePath)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                dataBaseMap[absoluteDatabasePath] = manager
            }
            return manager
        }
    }


}
