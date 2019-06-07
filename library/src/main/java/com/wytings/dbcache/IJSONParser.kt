package com.wytings.dbcache

import java.lang.reflect.Type

/**
 * Created by Rex.Wei on 2019-06-02.
 *
 * @author Rex.Wei
 */
interface IJSONParser {

    fun <T> from(json: String, typeOfT: Type): T?

    fun to(obj: Any): String

}
