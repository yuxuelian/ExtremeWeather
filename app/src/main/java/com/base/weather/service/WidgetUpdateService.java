package com.base.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/3/30
 * @author Administrator
 */

public class WidgetUpdateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
