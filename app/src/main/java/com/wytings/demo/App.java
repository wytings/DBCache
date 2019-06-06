package com.wytings.demo;

import android.app.Application;
import com.wytings.dbcache.DBCacheMgr;

/**
 * Created by Rex.Wei on 2019-06-07.
 *
 * @author 韦玉庭
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBCacheMgr.initialize(this);
    }
}
