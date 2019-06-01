package com.wytings.dbcache

import android.app.Application

/**
 * Created by Rex.Wei on 2019-06-02.
 *
 * @author 韦玉庭
 */
object DBCacheInitializer {

    fun initialize(application: Application) {
        AbsRoomDatabase.initialize(application)
    }

    fun initialize(application: Application, parser: IJSONParser) {
        AbsRoomDatabase.initialize(application)
        DBCacheMgr.jsonParser = parser
    }

}
