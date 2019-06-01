package com.wytings.dbcache

import android.util.Log

import java.lang.reflect.Type

/**
 * Created by Rex.Wei on 2019-06-02.
 *
 * @author 韦玉庭
 */
class EmptyJsonParser : IJSONParser {
    override fun <T> from(json: String, typeOfT: Type): T? {
        printError()
        return null
    }

    override fun to(obj: Any): String {
        printError()
        return ""
    }

    private fun printError() {
        Log.e(javaClass.simpleName, "ignore operation because empty implements IJSONParser")
    }
}
