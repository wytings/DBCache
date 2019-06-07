# DBCache
cache data with SQLite on Android

### When to use DBCache
SharePreferences or other local cache provider which are based on `File` will update whole file every time when anything need to update.
If this have affected the performance of your app, then DBCache may help you.

### Quick Tutorial
You can Setup DBCacheMgr on App startup, say your `App`, add these lines:

```Java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBCacheMgr.initialize(this);
    }
}
```

```Java
            private val zone = "user_info" // like cache fileName
            
            DBCacheMgr.obtain(zone).save("key1", 123)
            DBCacheMgr.obtain(zone).save("key2", 123.456)
            DBCacheMgr.obtain(zone).save("key3", -123)
            DBCacheMgr.obtain(zone).save("key4", "hello")
            DBCacheMgr.obtain(zone).save("key5", createMap())

            DBCacheMgr.obtain(zone).deleteAll()

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
```


### Download

Gradle:
```gradle
dependencies {
  implementation 'com.wytings.dbcache:dbcache:1.0.0'
}
```
