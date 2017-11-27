package com.as.weather;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

/**
 * 项目名称：ECanal
 * 公司：山东宇易信息科技
 * 作者：Administrator
 * 创建时间：2017/10/12 0012 13:46
 * 备注：
 */

public class MyApplication extends Application {

    public static String cache_dir;

    @Override
    public void onCreate() {
        super.onCreate();

        cache_dir = this.getCacheDir().getAbsolutePath();

        logUtilsInit();
        noHttpInit();
    }

    private void logUtilsInit() {
        //允许日志输出
        LogUtils.getLogConfig().configAllowLog(true)
                //日志Log前缀
                .configTagPrefix("ExtremeWeather")
                //是否显示编辑
                .configShowBorders(true)
                //自定义Tag
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");
    }

    private void noHttpInit() {
        //初始化NoHttp
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .connectionTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .build());
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("NoHttpExtremeWeather"); // 设置NoHttp打印Log的TAG。
    }
}
