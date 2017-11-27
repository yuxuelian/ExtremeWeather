package com.base.weather.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author 56896
 * @date 2017/1/1
 * 保存Bitmap到存储卡  或者从存储卡上读取Bitmap到内存
 */

public class BitmapFileUtil {

    private static final String TAG = "BitmapFileUtil";

    /**
     * @param bm   要保存的图片
     * @param path 保存路径
     * @param name 保存的文件名
     */
    public static void saveBitmap(Bitmap bm, String path, String name) {
        try {
            File f = new File(path, name);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘上获取Bitmap
     *
     * @param path
     * @param name
     * @return
     */
    public static Bitmap getBitmap(String path, String name) {
        try {
            return BitmapFactory.decodeFile(path + File.separator + name);
        } catch (Exception e) {
            return null;
        }
    }
}
