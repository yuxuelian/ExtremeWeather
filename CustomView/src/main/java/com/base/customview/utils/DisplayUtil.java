package com.base.customview.utils;

import android.content.Context;

/**
 * @author Administrator
 * @date 2017/11/27 22:14
 * GitHub：
 * email：
 * description：
 */
public class DisplayUtil {
    /**
     * convert px to its equivalent dp
     * <p>
     * 将px转换为与之相等的dp
     */
    public static float px2dp(Context context, float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().density;
    }


    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    public static float dp2px(Context context, float dipValue) {
        return dipValue * context.getResources().getDisplayMetrics().density;
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static float px2sp(Context context, float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().scaledDensity;
    }


    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static float sp2px(Context context, float spValue) {
        return spValue * context.getResources().getDisplayMetrics().scaledDensity;
    }
}
