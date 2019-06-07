package com.wytings.dbcache

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * Created by Rex.Wei on 2019-06-02.
 *
 * @author Rex.Wei
 */
class DefaultJsonParser : IJSONParser {

    private val helper = GsonBuilder().setLenient().setPrettyPrinting().create()

    override fun <T> from(json: String, typeOfT: Type): T? {
        return try {
            helper.fromJson(json, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun to(obj: Any): String {
        return try {
            helper.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
