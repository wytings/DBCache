package com.wytings.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wytings.dbcache.DBCacheMgr
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val zone = "zone_123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save.setOnClickListener {
            DBCacheMgr.obtain(zone).save("key1", 123)
            DBCacheMgr.obtain(zone).save("key2", 123.456)
            DBCacheMgr.obtain(zone).save("key3", -123)
            DBCacheMgr.obtain(zone).save("key4", "hello")
            DBCacheMgr.obtain(zone).save("key5", createMap())
        }

        delete.setOnClickListener {
            DBCacheMgr.obtain(zone).deleteAll()
        }

        read.setOnClickListener {
            DBCacheMgr.obtain(zone).getAll().forEach {
                println("${it.key} -> ${it.value}")
            }
            println(
                "$zone total count is ${DBCacheMgr.obtain(zone).getCount()}"
            )
            println(
                " key3 is ${DBCacheMgr.obtain(zone).getLong("key3", 10)}"
            )
        }
    }

    private fun createMap(): Map<String, Any> {
        return mapOf("1" to "haha", "2" to "hoho", "3" to 100)
    }


}
