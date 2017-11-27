package com.base.weather.util;

import android.support.annotation.DrawableRes;

import com.base.weather.R;

import java.util.Calendar;

/**
 * Created by 56896 on 2016/12/3.
 */

public class UpdateUIUtil {

    public static final int[] IC_WEATHER = new int[]{
            R.drawable.ic_100, R.drawable.ic_101, R.drawable.ic_104,
            R.drawable.ic_300, R.drawable.ic_302, R.drawable.ic_304,
            R.drawable.ic_306, R.drawable.ic_310, R.drawable.ic_311,
            R.drawable.ic_400, R.drawable.ic_401, R.drawable.ic_402,
            R.drawable.ic_403, R.drawable.ic_404, R.drawable.ic_501,
            R.drawable.ic_502, R.drawable.ic_503, R.drawable.ic_504,
            R.drawable.ic_507, R.drawable.ic_night1, R.drawable.ic_night2,
            R.drawable.ic_weather_kown
    };

    public static final int[] TEMP_NUM = new int[]{
            R.drawable.number_0, R.drawable.number_1, R.drawable.number_2, R.drawable.number_3,
            R.drawable.number_4, R.drawable.number_5, R.drawable.number_6, R.drawable.number_7,
            R.drawable.number_8, R.drawable.number_9
    };

    //将对应的天气代码转换成对应的图标id


    /**
     * @param weatherCode
     * @param isDay       是否是白天   true的话表示当前是白天,false表示当前是晚上
     * @return
     */
    public static
    @DrawableRes
    int getWeatherIcon(String weatherCode, boolean isDay) {
        if (weatherCode.equals("100")) {
            if (isDay) {
                return R.drawable.ic_100;
            } else {
                return R.drawable.ic_night1;
            }
        } else if (weatherCode.equals("101")) {
            if (isDay) {
                return R.drawable.ic_101;
            } else {
                return R.drawable.ic_night2;
            }
        } else if (weatherCode.equals("104")) {
            return R.drawable.ic_104;
        } else if (weatherCode.equals("300")) {
            return R.drawable.ic_300;
        } else if (weatherCode.equals("302")) {
            return R.drawable.ic_302;
        } else if (weatherCode.equals("304")) {
            return R.drawable.ic_304;
        } else if (weatherCode.equals("306")) {
            return R.drawable.ic_306;
        } else if (weatherCode.equals("310")) {
            return R.drawable.ic_310;
        } else if (weatherCode.equals("311")) {
            return R.drawable.ic_311;
        } else if (weatherCode.equals("400")) {
            return R.drawable.ic_400;
        } else if (weatherCode.equals("401")) {
            return R.drawable.ic_401;
        } else if (weatherCode.equals("402")) {
            return R.drawable.ic_402;
        } else if (weatherCode.equals("403")) {
            return R.drawable.ic_403;
        } else if (weatherCode.equals("404")) {
            return R.drawable.ic_404;
        } else if (weatherCode.equals("501")) {
            return R.drawable.ic_501;
        } else if (weatherCode.equals("502")) {
            return R.drawable.ic_502;
        } else if (weatherCode.equals("503")) {
            return R.drawable.ic_503;
        } else if (weatherCode.equals("504")) {
            return R.drawable.ic_504;
        } else if (weatherCode.equals("507")) {
            return R.drawable.ic_507;
        }
        return R.drawable.ic_weather_kown;
    }

    /**
     * 根据污染指数获取当前的污染图标
     * 优，良，轻度污染，中度污染，重度污染，严重污染
     *
     * @param qlty
     * @return
     */
    public static
    @DrawableRes
    int getQltyIcon(String qlty) {
        if (qlty.equals("优")) {
            return R.drawable.quality_1;
        } else if (qlty.equals("良")) {
            return R.drawable.quality_2;
        } else if (qlty.equals("轻度污染")) {
            return R.drawable.quality_3;
        } else if (qlty.equals("中度污染")) {
            return R.drawable.quality_4;
        } else if (qlty.equals("重度污染")) {
            return R.drawable.quality_5;
        } else if (qlty.equals("严重污染")) {
            return R.drawable.quality_6;
        } else {
            //默认
            return R.drawable.quality_3;
        }
    }


    /**
     * 由日出日落时间判断当前是白天还是晚上
     * <p>
     * 返回true则是白天
     * <p>
     * 返回false则是晚上
     *
     * @param sunrise
     * @param sunset
     * @return
     */
    public static boolean isDaytime(String sunrise, String sunset) {
        boolean is;
        //00:00
        int riseHour = Integer.parseInt(sunrise.substring(0, 2));
        int riseMinute = Integer.parseInt(sunrise.substring(3, 5));
        int setHour = Integer.parseInt(sunset.substring(0, 2));
        int setMinute = Integer.parseInt(sunset.substring(3, 5));

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        if ((currentHour > riseHour) && (currentHour < setHour)) {
            is = true;
        } else if (currentHour == riseHour) {
            if (currentMinute >= riseMinute) {
                is = true;
            } else {
                is = false;
            }
        } else if (currentHour == setHour) {
            if (currentMinute < setMinute) {
                is = true;
            } else {
                is = false;
            }
        } else {
            is = false;
        }
        return is;
    }
}
