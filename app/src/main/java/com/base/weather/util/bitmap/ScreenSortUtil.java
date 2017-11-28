package com.base.weather.util.bitmap;

import android.graphics.Bitmap;
import android.view.View;

/**
 * @author Administrator
 * @date 2017/11/27 0027 15:52
 * @description
 */
public class ScreenSortUtil {

    public static Bitmap screenSort(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap copy = drawingCache.copy(drawingCache.getConfig(), false);
        return copy;
    }

}
