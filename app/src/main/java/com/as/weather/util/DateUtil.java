package com.as.weather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 56896 on 2016/12/2.
 */

public class DateUtil {

    //根据当前的日期获取未来五天的日期
    public static String getFutureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String result = format.format(today);
        return result.substring(5,result.length());
    }
}
