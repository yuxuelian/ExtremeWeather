package com.qit.citywheelview.wheelview.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/4/17 10:58
 * 备注：城市数据库操作类
 */

public class CityDatabaseUtil {

    private static final String allCity = "city.db";

    private SQLiteDatabase sqLiteDatabase;
    private String string;

    public CityDatabaseUtil(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String path = context.getFilesDir().getAbsolutePath() + File.separator + CityDatabaseUtil.allCity;
        File file = new File(path);
        //如果不存在的话,就进行copy
        if (!file.exists()) {
            try {
                file.createNewFile();
                byte[] buf = new byte[512];
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = context.getAssets().open("city.db");
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
    }


    /**
     * 查询所有城市的数据库,获取全国所有的省
     *
     * @return
     */
    public List<String> getProvince() {
        List<String> provinces = new ArrayList<>();

        //distinct 去重
        Cursor cursor = sqLiteDatabase.rawQuery("select distinct provinceZh from city", null);
        if (cursor.moveToFirst()) {
            do {
                provinces.add(cursor.getString(cursor.getColumnIndex("provinceZh")));
            } while (cursor.moveToNext());
        }
        return provinces;
    }

    /**
     * 指定省份  然后去查询该省下所有的市
     *
     * @param province
     * @return
     */
    public List<String> getLeaders(String province) {
        List<String> leaders = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select distinct leaderZh from city where provinceZh=?", new String[]{province});
        if (cursor.moveToFirst()) {
            do {
                leaders.add(cursor.getString(cursor.getColumnIndex("leaderZh")));
            } while (cursor.moveToNext());
        }
        return leaders;
    }

    /**
     * 指定市 查询市下所对应的所有的区
     *
     * @param leader
     * @return
     */
    public List<String> getCities(String leader) {
        List<String> cities = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select distinct cityZh from city where leaderZh=?", new String[]{leader});
        if (cursor.moveToFirst()) {
            do {
                cities.add(cursor.getString(cursor.getColumnIndex("cityZh")));
            } while (cursor.moveToNext());
        }
        return cities;
    }

    public String getCityId(String city) {
        String string = null;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from city where cityZh=?", new String[]{city});
        if (cursor.moveToFirst()) {
            string = cursor.getString(cursor.getColumnIndex("id"))
                    + "|"
                    + city;
        }
        return string;
    }

    public void closeDb() {
        sqLiteDatabase.close();
    }
}
