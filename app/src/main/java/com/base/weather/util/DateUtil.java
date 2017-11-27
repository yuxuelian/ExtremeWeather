package com.base.weather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 56896
 * @date 2016/12/2
 */

public class DateUtil {

    /**
     * 根据当前的日期获取未来五天的日期
     *
     * @param past
     * @return
     */
    public static String getFutureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String result = format.format(today);
        return result.substring(5, result.length());
    }
}
