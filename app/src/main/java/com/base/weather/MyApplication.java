package com.base.weather;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


/**
 *
 *
 * 作者：Administrator
 * 创建时间：2017/10/12 0012 13:46
 * 备注：
 * @author Administrator
 */

public class MyApplication extends Application {

    /**
     * 缓存路径
     */
    public static String CACHE_DIR;

    @Override
    public void onCreate() {
        super.onCreate();

        //获取缓存路径
        CACHE_DIR = this.getCacheDir().getAbsolutePath();

        //初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}
